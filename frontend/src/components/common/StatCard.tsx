import clsx from 'clsx'

interface StatCardProps {
  label: string
  value: number | string
  icon: React.ReactNode
  color?: string
  sub?: string
}

export default function StatCard({ label, value, icon, color = 'text-brand-400', sub }: StatCardProps) {
  return (
    <div className="stat-card">
      <div className="flex items-start justify-between">
        <p className="text-sm text-slate-400">{label}</p>
        <span className={clsx('p-2 bg-surface-muted rounded-lg', color)}>{icon}</span>
      </div>
      <div>
        <p className="text-3xl font-display font-semibold text-white">{value.toLocaleString()}</p>
        {sub && <p className="text-xs text-slate-500 mt-0.5">{sub}</p>}
      </div>
    </div>
  )
}
