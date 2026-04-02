import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { spacesApi } from '@/api/spaces'
import { floorsApi } from '@/api/floors'
import { useForm } from 'react-hook-form'
import { Plus, Layout, ArrowLeft, Cpu, Trash2, Upload } from 'lucide-react'
import Modal from '@/components/common/Modal'
import EmptyState from '@/components/common/EmptyState'
import PageHeader from '@/components/common/PageHeader'
import Spinner from '@/components/common/Spinner'
import Badge from '@/components/common/Badge'
import BulkImportModal from '@/components/survey/BulkImportModal'
import toast from 'react-hot-toast'
import type { Space } from '@/types'

const SPACE_TYPES = [
  'APARTMENT','OFFICE','SERVER_ROOM','UTILITY',
  'CORRIDOR','LOBBY','ROOFTOP','BASEMENT','PARKING','OTHER'
]

const typeColor: Record<string, 'blue' | 'green' | 'yellow' | 'red' | 'slate'> = {
  SERVER_ROOM: 'red', ROOFTOP: 'yellow', APARTMENT: 'blue',
  OFFICE: 'green', LOBBY: 'slate',
}

export default function SpacesPage() {
  const { floorId } = useParams<{ floorId: string }>()
  const navigate = useNavigate()
  const qc = useQueryClient()
  const [open, setOpen] = useState(false)
  const [importOpen, setImportOpen] = useState(false)

  const { data: floor } = useQuery({
    queryKey: ['floor', floorId],
    queryFn: () => floorsApi.get(Number(floorId)),
  })

  const { data: spaces, isLoading } = useQuery({
    queryKey: ['spaces', floorId],
    queryFn: () => spacesApi.listByFloor(Number(floorId)),
  })

  const { register, handleSubmit, reset } = useForm<Partial<Space>>()

  const createMutation = useMutation({
    mutationFn: (d: Partial<Space>) => spacesApi.create({ ...d, floorId: Number(floorId) }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['spaces'] })
      toast.success('Space added')
      setOpen(false)
      reset()
    },
    onError: () => toast.error('Failed to create space'),
  })

  const deleteMutation = useMutation({
    mutationFn: spacesApi.delete,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['spaces'] }); toast.success('Space deleted') },
  })

  return (
    <div className="space-y-6 animate-slide-up">
      <div className="flex items-center gap-3">
        <button onClick={() => navigate(-1)} className="btn-ghost p-2"><ArrowLeft size={18}/></button>
        <PageHeader
          title={`Spaces — Floor ${floor?.levelLabel ?? ''}`}
          description="All tagged spaces on this floor"
          action={
            <div className="flex gap-2">
              <button onClick={() => setImportOpen(true)} className="btn-ghost border border-surface-border">
                <Upload size={15}/> Bulk Import
              </button>
              <button onClick={() => setOpen(true)} className="btn-primary">
                <Plus size={16}/> Add Space
              </button>
            </div>
          }
        />
      </div>

      {isLoading ? (
        <div className="flex justify-center py-16"><Spinner size={32}/></div>
      ) : !spaces?.length ? (
        <EmptyState icon={<Layout size={36}/>} title="No spaces yet"
          description="Add spaces manually or bulk import from CSV/XLSX"
          action={
            <div className="flex gap-2">
              <button onClick={() => setImportOpen(true)} className="btn-ghost border border-surface-border">
                <Upload size={15}/> Import CSV
              </button>
              <button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Add Space</button>
            </div>
          }
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
          {spaces.map(s => (
            <div key={s.id} className="card p-5 hover:border-brand-700/50 transition-all group">
              <div className="flex items-start justify-between mb-3">
                <div className="p-2.5 bg-teal-600/15 rounded-xl"><Layout size={18} className="text-teal-400"/></div>
                <div className="flex items-center gap-2">
                  <Badge label={s.type} variant={typeColor[s.type] ?? 'slate'} />
                  <button onClick={() => deleteMutation.mutate(s.id)}
                    className="text-slate-600 hover:text-red-400 opacity-0 group-hover:opacity-100 transition-all">
                    <Trash2 size={14}/>
                  </button>
                </div>
              </div>
              <h3 className="font-semibold text-white mb-1">{s.name}</h3>
              {s.areaSqM && <p className="text-xs text-slate-500 mb-3">{s.areaSqM} m²</p>}
              {s.notes && <p className="text-xs text-slate-500 line-clamp-2 mb-3">{s.notes}</p>}
              <button
                onClick={() => navigate(`/spaces/${s.id}/equipment`)}
                className="btn-ghost w-full justify-center border border-surface-border text-sm">
                <Cpu size={13}/> View Equipment
              </button>
            </div>
          ))}
        </div>
      )}

      {/* Add Space Modal */}
      <Modal title="Add Space" open={open} onClose={() => { setOpen(false); reset() }}>
        <form onSubmit={handleSubmit(d => createMutation.mutate(d))} className="space-y-4">
          <div>
            <label className="label">Space Name *</label>
            <input {...register('name', { required: true })} className="input" placeholder="e.g. Apartment 101" />
          </div>
          <div>
            <label className="label">Type</label>
            <select {...register('type')} className="input">
              {SPACE_TYPES.map(t => <option key={t} value={t}>{t.replace('_', ' ')}</option>)}
            </select>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="label">Area (m²)</label>
              <input {...register('areaSqM', { valueAsNumber: true })} type="number" step="0.1" className="input" />
            </div>
            <div>
              <label className="label">Elevation (m)</label>
              <input {...register('elevationM', { valueAsNumber: true })} type="number" step="0.1" className="input" />
            </div>
          </div>
          <div>
            <label className="label">Notes</label>
            <textarea {...register('notes')} className="input min-h-[80px] resize-none" placeholder="Additional details…" />
          </div>
          <div className="flex justify-end gap-3 pt-2">
            <button type="button" onClick={() => setOpen(false)} className="btn-ghost">Cancel</button>
            <button type="submit" disabled={createMutation.isPending} className="btn-primary">
              {createMutation.isPending ? 'Adding…' : 'Add Space'}
            </button>
          </div>
        </form>
      </Modal>

      <BulkImportModal
        open={importOpen}
        floorId={Number(floorId)}
        onClose={() => setImportOpen(false)}
        onSuccess={() => qc.invalidateQueries({ queryKey: ['spaces'] })}
      />
    </div>
  )
}
