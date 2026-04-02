import { useQuery } from '@tanstack/react-query'
import { dashboardApi } from '@/api/dashboard'
import { useAuthStore } from '@/store/authStore'
import StatsGrid from '@/components/dashboard/StatsGrid'
import Spinner from '@/components/common/Spinner'
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from 'recharts'
import { TrendingUp, Activity } from 'lucide-react'

const mockActivity = [
  { day: 'Mon', surveys: 4 },
  { day: 'Tue', surveys: 7 },
  { day: 'Wed', surveys: 3 },
  { day: 'Thu', surveys: 9 },
  { day: 'Fri', surveys: 6 },
  { day: 'Sat', surveys: 2 },
  { day: 'Sun', surveys: 5 },
]

export default function DashboardPage() {
  const orgId = useAuthStore(s => s.orgId)

  const { data: stats, isLoading } = useQuery({
    queryKey: ['dashboard', orgId],
    queryFn: () => dashboardApi.getStats(orgId),
  })

  if (isLoading) return (
    <div className="flex items-center justify-center h-64">
      <Spinner size={32} />
    </div>
  )

  const defaultStats = stats ?? {
    totalProperties: 0, totalBuildings: 0, totalFloors: 0,
    totalSpaces: 0, totalEquipment: 0, totalChecklistResponses: 0, pendingReports: 0,
  }

  return (
    <div className="space-y-6 animate-slide-up">
      <div>
        <h2 className="font-display font-bold text-2xl text-white">Overview</h2>
        <p className="text-slate-400 text-sm mt-0.5">Organisation-wide network survey metrics</p>
      </div>

      <StatsGrid stats={defaultStats} />

      <div className="grid grid-cols-1 xl:grid-cols-2 gap-4">
        {/* Activity Chart */}
        <div className="card p-5">
          <div className="flex items-center gap-2 mb-5">
            <Activity size={16} className="text-brand-400" />
            <h3 className="font-medium text-white">Survey Activity (This Week)</h3>
          </div>
          <ResponsiveContainer width="100%" height={180}>
            <BarChart data={mockActivity} barSize={28}>
              <XAxis dataKey="day" stroke="#475569" tick={{ fill: '#64748b', fontSize: 12 }} axisLine={false} tickLine={false} />
              <YAxis stroke="#475569" tick={{ fill: '#64748b', fontSize: 12 }} axisLine={false} tickLine={false} />
              <Tooltip
                contentStyle={{ background: '#161f2e', border: '1px solid #1e2d42', borderRadius: 12, color: '#e2e8f0' }}
                cursor={{ fill: 'rgba(43,135,251,0.08)' }}
              />
              <Bar dataKey="surveys" radius={[6, 6, 0, 0]}>
                {mockActivity.map((_, i) => (
                  <Cell key={i} fill={i === 3 ? '#2b87fb' : '#1e2d42'} />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Quick info */}
        <div className="card p-5">
          <div className="flex items-center gap-2 mb-5">
            <TrendingUp size={16} className="text-brand-400" />
            <h3 className="font-medium text-white">Survey Completion</h3>
          </div>
          <div className="space-y-4">
            {[
              { label: 'Floor plans uploaded',   pct: 72, color: 'bg-brand-500' },
              { label: 'Spaces documented',       pct: 58, color: 'bg-violet-500' },
              { label: 'Equipment catalogued',    pct: 43, color: 'bg-teal-500' },
              { label: 'Checklists completed',    pct: 81, color: 'bg-green-500' },
              { label: 'RF scans done',           pct: 34, color: 'bg-orange-500' },
            ].map(({ label, pct, color }) => (
              <div key={label}>
                <div className="flex justify-between text-sm mb-1.5">
                  <span className="text-slate-400">{label}</span>
                  <span className="text-slate-300 font-mono text-xs">{pct}%</span>
                </div>
                <div className="h-1.5 bg-surface-muted rounded-full overflow-hidden">
                  <div className={`h-full ${color} rounded-full transition-all duration-700`} style={{ width: `${pct}%` }} />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
