import { useParams, useNavigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { propertiesApi } from '@/api/properties'
import { Building2, ArrowLeft, MapPin, Radio } from 'lucide-react'
import Spinner from '@/components/common/Spinner'

export default function PropertyDetailPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()

  const { data: property, isLoading } = useQuery({
    queryKey: ['property', id],
    queryFn: () => propertiesApi.get(Number(id)),
  })

  if (isLoading) return <div className="flex justify-center py-16"><Spinner size={32}/></div>
  if (!property) return <div className="text-slate-400">Property not found</div>

  return (
    <div className="space-y-6 animate-slide-up">
      <div className="flex items-center gap-3">
        <button onClick={() => navigate(-1)} className="btn-ghost p-2">
          <ArrowLeft size={18}/>
        </button>
        <div>
          <h2 className="font-display font-bold text-2xl text-white">{property.name}</h2>
          <p className="text-slate-400 text-sm">{[property.city, property.state, property.country].filter(Boolean).join(', ')}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <button onClick={() => navigate(`/properties/${id}/buildings`)}
          className="card p-6 flex flex-col gap-3 hover:border-brand-700/50 transition-all text-left">
          <div className="p-3 bg-violet-600/15 rounded-xl w-fit"><Building2 size={22} className="text-violet-400"/></div>
          <div>
            <p className="font-semibold text-white">Buildings</p>
            <p className="text-sm text-slate-500">Manage buildings and floors</p>
          </div>
        </button>
        <button onClick={() => navigate(`/checklists`)}
          className="card p-6 flex flex-col gap-3 hover:border-brand-700/50 transition-all text-left">
          <div className="p-3 bg-green-600/15 rounded-xl w-fit"><MapPin size={22} className="text-green-400"/></div>
          <div>
            <p className="font-semibold text-white">Surveys</p>
            <p className="text-sm text-slate-500">View and fill checklists</p>
          </div>
        </button>
        <button onClick={() => navigate(`/rf-scans`)}
          className="card p-6 flex flex-col gap-3 hover:border-brand-700/50 transition-all text-left">
          <div className="p-3 bg-orange-600/15 rounded-xl w-fit"><Radio size={22} className="text-orange-400"/></div>
          <div>
            <p className="font-semibold text-white">RF Scans</p>
            <p className="text-sm text-slate-500">View signal coverage</p>
          </div>
        </button>
      </div>

      <div className="card p-5">
        <h3 className="font-medium text-white mb-4">Property Details</h3>
        <dl className="grid grid-cols-2 gap-4">
          {[
            ['Address', [property.addressLine1, property.addressLine2].filter(Boolean).join(', ') || '—'],
            ['City', property.city || '—'],
            ['State', property.state || '—'],
            ['Postal Code', property.postalCode || '—'],
            ['Country', property.country || '—'],
            ['Coordinates', property.centroidLat ? `${property.centroidLat}, ${property.centroidLon}` : '—'],
          ].map(([k, v]) => (
            <div key={k}>
              <dt className="text-xs text-slate-500 uppercase tracking-wide">{k}</dt>
              <dd className="text-sm text-slate-200 mt-0.5">{v}</dd>
            </div>
          ))}
        </dl>
      </div>
    </div>
  )
}
