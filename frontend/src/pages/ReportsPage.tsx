import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { reportsApi } from '@/api/reports'
import { propertiesApi } from '@/api/properties'
import { useAuthStore } from '@/store/authStore'
import { FileBarChart2, Plus, Download, RefreshCw } from 'lucide-react'
import Modal from '@/components/common/Modal'
import EmptyState from '@/components/common/EmptyState'
import PageHeader from '@/components/common/PageHeader'
import Spinner from '@/components/common/Spinner'
import Badge from '@/components/common/Badge'
import toast from 'react-hot-toast'
import { format } from 'date-fns'

const statusColor: Record<string, 'blue' | 'yellow' | 'green' | 'red'> = {
  PENDING: 'yellow', GENERATING: 'blue', DONE: 'green', FAILED: 'red',
}

export default function ReportsPage() {
  const orgId = useAuthStore(s => s.orgId)
  const qc = useQueryClient()
  const [open, setOpen] = useState(false)
  const [selProp, setSelProp] = useState<number | null>(null)

  const { data: propertiesData } = useQuery({
    queryKey: ['properties', orgId],
    queryFn: () => propertiesApi.list(orgId),
  })

  const { data: reports, isLoading } = useQuery({
    queryKey: ['reports', selProp],
    queryFn: () => selProp ? reportsApi.list(selProp) : Promise.resolve([]),
    enabled: !!selProp,
    refetchInterval: 5000,
  })

  const generateMutation = useMutation({
    mutationFn: (propertyId: number) => reportsApi.generate(propertyId),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['reports'] }); toast.success('Report queued for generation'); setOpen(false) },
    onError: () => toast.error('Failed to generate report'),
  })

  return (
    <div className="space-y-6 animate-slide-up">
      <PageHeader
        title="Reports"
        description="Generate and download PDF site survey reports"
        action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Generate Report</button>}
      />

      <div className="flex gap-2 flex-wrap">
        {propertiesData?.content?.map(p => (
          <button key={p.id} onClick={() => setSelProp(p.id)}
            className={`px-3 py-1.5 rounded-lg text-sm font-medium transition-all border
              ${selProp === p.id
                ? 'bg-brand-600/20 border-brand-600/50 text-brand-300'
                : 'bg-surface-muted border-surface-border text-slate-400 hover:text-slate-200'}`}>
            {p.name}
          </button>
        ))}
      </div>

      {!selProp ? (
        <EmptyState icon={<FileBarChart2 size={36}/>} title="Select a property" description="Choose a property to view its reports" />
      ) : isLoading ? (
        <div className="flex justify-center py-16"><Spinner size={32}/></div>
      ) : !reports?.length ? (
        <EmptyState icon={<FileBarChart2 size={36}/>} title="No reports yet"
          action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Generate Report</button>} />
      ) : (
        <div className="space-y-3">
          {reports.map(r => (
            <div key={r.id} className="card p-5 flex items-center gap-4">
              <div className="p-3 bg-amber-600/15 rounded-xl shrink-0"><FileBarChart2 size={20} className="text-amber-400"/></div>
              <div className="flex-1">
                <div className="flex items-center gap-2 mb-1">
                  <span className="font-medium text-white">Report #{r.id}</span>
                  <Badge label={r.status} variant={statusColor[r.status] ?? 'slate'}/>
                </div>
                <p className="text-sm text-slate-500">{format(new Date(r.createdAt), 'dd MMM yyyy, HH:mm')}</p>
              </div>
              {r.status === 'DONE' && (
                <button className="btn-ghost p-2 text-green-400"><Download size={16}/></button>
              )}
              {r.status === 'GENERATING' && (
                <RefreshCw size={16} className="text-brand-400 animate-spin"/>
              )}
            </div>
          ))}
        </div>
      )}

      <Modal title="Generate Report" open={open} onClose={() => setOpen(false)}>
        <div className="space-y-4">
          <p className="text-sm text-slate-400">Select a property to generate a full PDF survey report.</p>
          <div className="space-y-2">
            {propertiesData?.content?.map(p => (
              <button key={p.id}
                onClick={() => generateMutation.mutate(p.id)}
                disabled={generateMutation.isPending}
                className="w-full card p-4 flex items-center gap-3 hover:border-brand-700/50 transition-all text-left">
                <FileBarChart2 size={16} className="text-amber-400 shrink-0"/>
                <span className="text-sm text-white">{p.name}</span>
              </button>
            ))}
          </div>
          <div className="flex justify-end pt-2">
            <button onClick={() => setOpen(false)} className="btn-ghost">Cancel</button>
          </div>
        </div>
      </Modal>
    </div>
  )
}
