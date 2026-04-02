import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { floorsApi } from '@/api/floors'
import { useForm } from 'react-hook-form'
import { Plus, Layers, ArrowLeft, Map, Trash2 } from 'lucide-react'
import Modal from '@/components/common/Modal'
import EmptyState from '@/components/common/EmptyState'
import PageHeader from '@/components/common/PageHeader'
import Spinner from '@/components/common/Spinner'
import toast from 'react-hot-toast'
import type { Floor } from '@/types'

export default function FloorsPage() {
  const { buildingId } = useParams<{ buildingId: string }>()
  const navigate = useNavigate()
  const qc = useQueryClient()
  const [open, setOpen] = useState(false)

  const { data: floors, isLoading } = useQuery({
    queryKey: ['floors', buildingId],
    queryFn: () => floorsApi.listByBuilding(Number(buildingId)),
  })

  const { register, handleSubmit, reset } = useForm<Partial<Floor>>()

  const createMutation = useMutation({
    mutationFn: (d: Partial<Floor>) => floorsApi.create({ ...d, buildingId: Number(buildingId) }),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['floors'] }); toast.success('Floor added'); setOpen(false); reset() },
  })

  const deleteMutation = useMutation({
    mutationFn: floorsApi.delete,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['floors'] }); toast.success('Floor deleted') },
  })

  return (
    <div className="space-y-6 animate-slide-up">
      <div className="flex items-center gap-3">
        <button onClick={() => navigate(-1)} className="btn-ghost p-2"><ArrowLeft size={18}/></button>
        <PageHeader
          title="Floors"
          description={`Building #${buildingId} — all floors`}
          action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Add Floor</button>}
        />
      </div>

      {isLoading ? (
        <div className="flex justify-center py-16"><Spinner size={32}/></div>
      ) : !floors?.length ? (
        <EmptyState icon={<Layers size={36}/>} title="No floors yet"
          action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Add Floor</button>} />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
          {floors.map((f) => (
            <div key={f.id} className="card p-5 hover:border-brand-700/50 transition-all group">
              <div className="flex items-start justify-between mb-4">
                <div className="p-2.5 bg-sky-600/15 rounded-xl"><Layers size={20} className="text-sky-400"/></div>
                <button onClick={() => deleteMutation.mutate(f.id)}
                  className="text-slate-600 hover:text-red-400 opacity-0 group-hover:opacity-100 transition-all">
                  <Trash2 size={15}/>
                </button>
              </div>
              <h3 className="font-semibold text-white mb-1">Floor {f.levelLabel}</h3>
              <p className="text-sm text-slate-500 mb-4">Elevation: {f.elevationM ?? 0}m</p>
              <div className="flex gap-2">
                <button onClick={() => navigate(`/floors/${f.id}/plan`)}
                  className="btn-primary text-sm flex-1 justify-center">
                  <Map size={14}/> Floor Plan
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      <Modal title="Add Floor" open={open} onClose={() => { setOpen(false); reset() }}>
        <form onSubmit={handleSubmit((d) => createMutation.mutate(d))} className="space-y-4">
          <div>
            <label className="label">Level Label *</label>
            <input {...register('levelLabel', { required: true })} className="input" placeholder="e.g. G, 1, 2, B1" />
          </div>
          <div>
            <label className="label">Elevation (m)</label>
            <input {...register('elevationM', { valueAsNumber: true })} type="number" step="0.1" className="input" defaultValue={0} />
          </div>
          <div>
            <label className="label">Scale Ratio</label>
            <input {...register('scaleRatio')} className="input" placeholder="e.g. 1:100" />
          </div>
          <div className="flex justify-end gap-3 pt-2">
            <button type="button" onClick={() => setOpen(false)} className="btn-ghost">Cancel</button>
            <button type="submit" disabled={createMutation.isPending} className="btn-primary">
              {createMutation.isPending ? 'Creating…' : 'Add Floor'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
