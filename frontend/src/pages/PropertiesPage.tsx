import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { propertiesApi } from '@/api/properties'
import { useAuthStore } from '@/store/authStore'
import { useForm } from 'react-hook-form'
import { Plus, MapPin, Building2, ArrowRight, Trash2, Globe } from 'lucide-react'
import Modal from '@/components/common/Modal'
import EmptyState from '@/components/common/EmptyState'
import PageHeader from '@/components/common/PageHeader'
import Spinner from '@/components/common/Spinner'
import toast from 'react-hot-toast'
import type { Property } from '@/types'

export default function PropertiesPage() {
  const orgId = useAuthStore(s => s.orgId)
  const navigate = useNavigate()
  const qc = useQueryClient()
  const [open, setOpen] = useState(false)

  const { data, isLoading } = useQuery({
    queryKey: ['properties', orgId],
    queryFn: () => propertiesApi.list(orgId),
  })

  const { register, handleSubmit, reset } = useForm<Partial<Property>>()

  const createMutation = useMutation({
    mutationFn: (d: Partial<Property>) => propertiesApi.create({ ...d, organizationId: orgId }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['properties'] })
      toast.success('Property created')
      setOpen(false)
      reset()
    },
    onError: () => toast.error('Failed to create property'),
  })

  const deleteMutation = useMutation({
    mutationFn: propertiesApi.delete,
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['properties'] })
      toast.success('Property deleted')
    },
  })

  return (
    <div className="space-y-6 animate-slide-up">
      <PageHeader
        title="Properties"
        description="Manage sites, campuses, and large properties"
        action={
          <button onClick={() => setOpen(true)} className="btn-primary">
            <Plus size={16}/> Add Property
          </button>
        }
      />

      {isLoading ? (
        <div className="flex justify-center py-16"><Spinner size={32}/></div>
      ) : !data?.content?.length ? (
        <EmptyState
          icon={<MapPin size={36}/>}
          title="No properties yet"
          description="Add your first property to start a site survey"
          action={<button onClick={() => setOpen(true)} className="btn-primary"><Plus size={16}/>Add Property</button>}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
          {data.content.map((p) => (
            <div key={p.id} className="card p-5 hover:border-brand-700/50 transition-all duration-200 group">
              <div className="flex items-start justify-between mb-4">
                <div className="p-2.5 bg-brand-600/15 rounded-xl">
                  <MapPin size={20} className="text-brand-400"/>
                </div>
                <button
                  onClick={(e) => { e.stopPropagation(); deleteMutation.mutate(p.id) }}
                  className="text-slate-600 hover:text-red-400 transition-colors opacity-0 group-hover:opacity-100"
                >
                  <Trash2 size={15}/>
                </button>
              </div>
              <h3 className="font-semibold text-white mb-1 truncate">{p.name}</h3>
              <p className="text-sm text-slate-500 flex items-center gap-1.5 mb-4">
                <Globe size={12}/>
                {[p.city, p.state, p.country].filter(Boolean).join(', ') || 'No location'}
              </p>
              <div className="flex items-center gap-2">
                <button
                  onClick={() => navigate(`/properties/${p.id}/buildings`)}
                  className="btn-ghost text-sm flex-1 justify-center border border-surface-border"
                >
                  <Building2 size={14}/> Buildings
                </button>
                <button
                  onClick={() => navigate(`/properties/${p.id}`)}
                  className="btn-primary text-sm px-3"
                >
                  <ArrowRight size={14}/>
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      <Modal title="Add Property" open={open} onClose={() => { setOpen(false); reset() }} size="lg">
        <form onSubmit={handleSubmit((d) => createMutation.mutate(d))} className="space-y-4">
          <div>
            <label className="label">Property Name *</label>
            <input {...register('name', { required: true })} className="input" placeholder="e.g. Sunrise Apartments Complex" />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="label">Address Line 1</label>
              <input {...register('addressLine1')} className="input" placeholder="Street address" />
            </div>
            <div>
              <label className="label">Address Line 2</label>
              <input {...register('addressLine2')} className="input" placeholder="Suite, floor…" />
            </div>
          </div>
          <div className="grid grid-cols-3 gap-4">
            <div>
              <label className="label">City</label>
              <input {...register('city')} className="input" placeholder="City" />
            </div>
            <div>
              <label className="label">State</label>
              <input {...register('state')} className="input" placeholder="State" />
            </div>
            <div>
              <label className="label">Country</label>
              <input {...register('country')} className="input" placeholder="Country" />
            </div>
          </div>
          <div className="flex justify-end gap-3 pt-2">
            <button type="button" onClick={() => setOpen(false)} className="btn-ghost">Cancel</button>
            <button type="submit" disabled={createMutation.isPending} className="btn-primary">
              {createMutation.isPending ? 'Creating…' : 'Create Property'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
