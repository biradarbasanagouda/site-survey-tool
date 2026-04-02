import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { buildingsApi } from '@/api/buildings'
import { useForm } from 'react-hook-form'
import { Plus, Building2, ArrowLeft, Layers, Trash2 } from 'lucide-react'
import Modal from '@/components/common/Modal'
import EmptyState from '@/components/common/EmptyState'
import PageHeader from '@/components/common/PageHeader'
import Spinner from '@/components/common/Spinner'
import toast from 'react-hot-toast'
import type { Building } from '@/types'

export default function BuildingsPage() {
  const { propertyId } = useParams<{ propertyId: string }>()
  const navigate = useNavigate()
  const qc = useQueryClient()
  const [open, setOpen] = useState(false)

  const { data: buildings, isLoading } = useQuery({
    queryKey: ['buildings', propertyId],
    queryFn: () => buildingsApi.listByProperty(Number(propertyId)),
  })

  const { register, handleSubmit, reset } = useForm<Partial<Building>>()

  const createMutation = useMutation({
    mutationFn: (d: Partial<Building>) => buildingsApi.create({ ...d, propertyId: Number(propertyId) }),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['buildings'] }); toast.success('Building added'); setOpen(false); reset() },
    onError: () => toast.error('Failed to create building'),
  })

  const deleteMutation = useMutation({
    mutationFn: buildingsApi.delete,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['buildings'] }); toast.success('Building deleted') },
  })

  return (
    <div className="space-y-6 animate-slide-up">
      <div className="flex items-center gap-3">
        <button onClick={() => navigate(-1)} className="btn-ghost p-2"><ArrowLeft size={18}/></button>
        <PageHeader
          title="Buildings"
          description={`Property #${propertyId} — all buildings`}
          action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Add Building</button>}
        />
      </div>

      {isLoading ? (
        <div className="flex justify-center py-16"><Spinner size={32}/></div>
      ) : !buildings?.length ? (
        <EmptyState icon={<Building2 size={36}/>} title="No buildings yet"
          action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Add Building</button>} />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
          {buildings.map((b) => (
            <div key={b.id} className="card p-5 hover:border-brand-700/50 transition-all group">
              <div className="flex items-start justify-between mb-4">
                <div className="p-2.5 bg-violet-600/15 rounded-xl"><Building2 size={20} className="text-violet-400"/></div>
                <button onClick={() => deleteMutation.mutate(b.id)}
                  className="text-slate-600 hover:text-red-400 opacity-0 group-hover:opacity-100 transition-all">
                  <Trash2 size={15}/>
                </button>
              </div>
              <h3 className="font-semibold text-white mb-1">{b.name}</h3>
              {b.code && <p className="text-xs text-slate-500 font-mono mb-3">Code: {b.code}</p>}
              <div className="flex items-center gap-2 text-sm text-slate-400 mb-4">
                <Layers size={13}/> {b.floorsCount} floor{b.floorsCount !== 1 ? 's' : ''}
              </div>
              <button onClick={() => navigate(`/buildings/${b.id}/floors`)}
                className="btn-ghost w-full justify-center border border-surface-border text-sm">
                <Layers size={14}/> View Floors
              </button>
            </div>
          ))}
        </div>
      )}

      <Modal title="Add Building" open={open} onClose={() => { setOpen(false); reset() }}>
        <form onSubmit={handleSubmit((d) => createMutation.mutate(d))} className="space-y-4">
          <div>
            <label className="label">Building Name *</label>
            <input {...register('name', { required: true })} className="input" placeholder="e.g. Block A" />
          </div>
          <div>
            <label className="label">Building Code</label>
            <input {...register('code')} className="input" placeholder="e.g. BLK-A" />
          </div>
          <div>
            <label className="label">Number of Floors</label>
            <input {...register('floorsCount', { valueAsNumber: true })} type="number" min={1} className="input" defaultValue={1} />
          </div>
          <div className="flex justify-end gap-3 pt-2">
            <button type="button" onClick={() => setOpen(false)} className="btn-ghost">Cancel</button>
            <button type="submit" disabled={createMutation.isPending} className="btn-primary">
              {createMutation.isPending ? 'Creating…' : 'Add Building'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
