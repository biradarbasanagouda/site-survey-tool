import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { equipmentApi } from '@/api/equipment'
import { spacesApi } from '@/api/spaces'
import { useForm } from 'react-hook-form'
import { Plus, Cpu, ArrowLeft, Trash2, Zap } from 'lucide-react'
import Modal from '@/components/common/Modal'
import EmptyState from '@/components/common/EmptyState'
import PageHeader from '@/components/common/PageHeader'
import Spinner from '@/components/common/Spinner'
import Badge from '@/components/common/Badge'
import toast from 'react-hot-toast'

const EQ_TYPES = [
  'ROUTER','SWITCH','ACCESS_POINT','ANTENNA','OLT',
  'ONT','CABINET','PATCH_PANEL','AMPLIFIER','SPLITTER','OTHER'
]

export default function EquipmentPage() {
  const { spaceId } = useParams<{ spaceId: string }>()
  const navigate = useNavigate()
  const qc = useQueryClient()
  const [open, setOpen] = useState(false)

  const { data: space } = useQuery({
    queryKey: ['space', spaceId],
    queryFn: () => spacesApi.get(Number(spaceId)),
  })

  const { data: equipment, isLoading } = useQuery({
    queryKey: ['equipment', spaceId],
    queryFn: () => equipmentApi.listBySpace(Number(spaceId)),
  })

  const { register, handleSubmit, reset } = useForm<{
    type: string; model: string; vendor: string; powerWatts: number; serialNumber: string
  }>()

  const createMutation = useMutation({
    mutationFn: (d: any) => equipmentApi.create({ ...d, spaceId: Number(spaceId) }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['equipment'] })
      toast.success('Equipment added')
      setOpen(false)
      reset()
    },
    onError: () => toast.error('Failed to add equipment'),
  })

  const deleteMutation = useMutation({
    mutationFn: equipmentApi.delete,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['equipment'] }); toast.success('Equipment removed') },
  })

  return (
    <div className="space-y-6 animate-slide-up">
      <div className="flex items-center gap-3">
        <button onClick={() => navigate(-1)} className="btn-ghost p-2"><ArrowLeft size={18}/></button>
        <PageHeader
          title={`Equipment — ${space?.name ?? 'Space'}`}
          description="Network equipment installed in this space"
          action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Add Equipment</button>}
        />
      </div>

      {isLoading ? (
        <div className="flex justify-center py-16"><Spinner size={32}/></div>
      ) : !equipment?.length ? (
        <EmptyState icon={<Cpu size={36}/>} title="No equipment catalogued"
          action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Add Equipment</button>} />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
          {equipment.map(eq => (
            <div key={eq.id} className="card p-5 hover:border-brand-700/50 transition-all group">
              <div className="flex items-start justify-between mb-3">
                <div className="p-2.5 bg-orange-600/15 rounded-xl"><Cpu size={18} className="text-orange-400"/></div>
                <div className="flex items-center gap-2">
                  <Badge label={eq.type} variant="slate" />
                  <button onClick={() => deleteMutation.mutate(eq.id)}
                    className="text-slate-600 hover:text-red-400 opacity-0 group-hover:opacity-100 transition-all">
                    <Trash2 size={14}/>
                  </button>
                </div>
              </div>
              <h3 className="font-semibold text-white mb-1">{eq.model || eq.type}</h3>
              {eq.vendor && <p className="text-xs text-slate-500 mb-2">Vendor: {eq.vendor}</p>}
              {eq.powerWatts && (
                <p className="text-xs text-slate-500 flex items-center gap-1">
                  <Zap size={11}/> {eq.powerWatts}W
                </p>
              )}
            </div>
          ))}
        </div>
      )}

      <Modal title="Add Equipment" open={open} onClose={() => { setOpen(false); reset() }}>
        <form onSubmit={handleSubmit(d => createMutation.mutate(d))} className="space-y-4">
          <div>
            <label className="label">Equipment Type *</label>
            <select {...register('type', { required: true })} className="input">
              {EQ_TYPES.map(t => <option key={t} value={t}>{t.replace('_', ' ')}</option>)}
            </select>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="label">Model</label>
              <input {...register('model')} className="input" placeholder="e.g. Cisco AIR-AP2802" />
            </div>
            <div>
              <label className="label">Vendor</label>
              <input {...register('vendor')} className="input" placeholder="e.g. Cisco" />
            </div>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="label">Power (W)</label>
              <input {...register('powerWatts', { valueAsNumber: true })} type="number" step="0.1" className="input" />
            </div>
            <div>
              <label className="label">Serial Number</label>
              <input {...register('serialNumber')} className="input" placeholder="SN-XXXX" />
            </div>
          </div>
          <div className="flex justify-end gap-3 pt-2">
            <button type="button" onClick={() => setOpen(false)} className="btn-ghost">Cancel</button>
            <button type="submit" disabled={createMutation.isPending} className="btn-primary">
              {createMutation.isPending ? 'Adding…' : 'Add Equipment'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
