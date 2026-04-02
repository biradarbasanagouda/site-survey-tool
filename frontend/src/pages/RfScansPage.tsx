import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'
import { Signal, Plus, Radio } from 'lucide-react'
import { useAuthStore } from '@/store/authStore'
import { propertiesApi } from '@/api/properties'
import api from '@/api/client'
import Modal from '@/components/common/Modal'
import EmptyState from '@/components/common/EmptyState'
import PageHeader from '@/components/common/PageHeader'
import Spinner from '@/components/common/Spinner'
import Badge from '@/components/common/Badge'
import toast from 'react-hot-toast'
import type { RfScan } from '@/types'
import { format } from 'date-fns'

const TOOLS = ['VISTUMBLER','KISMET','SPLAT','MANUAL','OTHER'] as const

export default function RfScansPage() {
  const orgId = useAuthStore(s => s.orgId)
  const qc = useQueryClient()
  const [open, setOpen] = useState(false)
  const [selectedProperty, setSelectedProperty] = useState<number | null>(null)

  const { data: propertiesData } = useQuery({
    queryKey: ['properties', orgId],
    queryFn: () => propertiesApi.list(orgId),
  })

  const { data: scans, isLoading } = useQuery({
    queryKey: ['rf-scans', selectedProperty],
    queryFn: () => selectedProperty
      ? api.get<RfScan[]>('/rf-scans', { params: { propertyId: selectedProperty } }).then(r => r.data)
      : Promise.resolve([] as RfScan[]),
    enabled: !!selectedProperty,
  })

  const { register, handleSubmit, reset } = useForm<{ propertyId: number; tool: string }>()

  const createMutation = useMutation({
    mutationFn: (d: { propertyId: number; tool: string }) =>
      api.post('/rf-scans', d).then(r => r.data),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['rf-scans'] }); toast.success('RF Scan logged'); setOpen(false); reset() },
  })

  const toolColor = (t: string) => t === 'MANUAL' ? 'slate' : t === 'KISMET' ? 'blue' : t === 'VISTUMBLER' ? 'green' : 'yellow'

  return (
    <div className="space-y-6 animate-slide-up">
      <PageHeader
        title="RF Scans"
        description="Upload and manage radio frequency coverage data"
        action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Log Scan</button>}
      />

      {/* Property filter */}
      <div className="flex gap-2 flex-wrap">
        {propertiesData?.content?.map(p => (
          <button key={p.id}
            onClick={() => setSelectedProperty(p.id)}
            className={`px-3 py-1.5 rounded-lg text-sm font-medium transition-all duration-150 border
              ${selectedProperty === p.id
                ? 'bg-brand-600/20 border-brand-600/50 text-brand-300'
                : 'bg-surface-muted border-surface-border text-slate-400 hover:text-slate-200'}`}>
            {p.name}
          </button>
        ))}
      </div>

      {!selectedProperty ? (
        <EmptyState icon={<Signal size={36}/>} title="Select a property" description="Choose a property above to view its RF scans" />
      ) : isLoading ? (
        <div className="flex justify-center py-16"><Spinner size={32}/></div>
      ) : !scans?.length ? (
        <EmptyState icon={<Radio size={36}/>} title="No RF scans yet"
          action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Log Scan</button>} />
      ) : (
        <div className="space-y-3">
          {scans.map(s => (
            <div key={s.id} className="card p-5 flex items-center gap-4">
              <div className="p-3 bg-orange-600/15 rounded-xl shrink-0"><Signal size={20} className="text-orange-400"/></div>
              <div className="flex-1">
                <div className="flex items-center gap-2 mb-1">
                  <span className="font-medium text-white">Scan #{s.id}</span>
                  <Badge label={s.tool} variant={toolColor(s.tool) as any}/>
                </div>
                <p className="text-sm text-slate-500">{format(new Date(s.createdAt), 'dd MMM yyyy, HH:mm')}</p>
              </div>
            </div>
          ))}
        </div>
      )}

      <Modal title="Log RF Scan" open={open} onClose={() => { setOpen(false); reset() }}>
        <form onSubmit={handleSubmit((d) => createMutation.mutate({ ...d, propertyId: Number(d.propertyId) }))} className="space-y-4">
          <div>
            <label className="label">Property *</label>
            <select {...register('propertyId', { required: true })} className="input">
              <option value="">Select property…</option>
              {propertiesData?.content?.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
            </select>
          </div>
          <div>
            <label className="label">Tool *</label>
            <select {...register('tool', { required: true })} className="input">
              {TOOLS.map(t => <option key={t} value={t}>{t}</option>)}
            </select>
          </div>
          <div className="flex justify-end gap-3 pt-2">
            <button type="button" onClick={() => setOpen(false)} className="btn-ghost">Cancel</button>
            <button type="submit" disabled={createMutation.isPending} className="btn-primary">
              {createMutation.isPending ? 'Saving…' : 'Log Scan'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
