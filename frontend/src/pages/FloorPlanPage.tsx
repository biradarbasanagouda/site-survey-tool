import { useParams, useNavigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { floorsApi } from '@/api/floors'
import { ArrowLeft, Upload, Layers } from 'lucide-react'
import Spinner from '@/components/common/Spinner'
import { useCallback, useState } from 'react'
import { useDropzone } from 'react-dropzone'
import toast from 'react-hot-toast'
import clsx from 'clsx'

export default function FloorPlanPage() {
  const { floorId } = useParams<{ floorId: string }>()
  const navigate = useNavigate()
  const [uploading, setUploading] = useState(false)
  const [planUrl, setPlanUrl] = useState<string | null>(null)

  const { data: floor, isLoading } = useQuery({
    queryKey: ['floor', floorId],
    queryFn: () => floorsApi.get(Number(floorId)),
  })

  const onDrop = useCallback(async (files: File[]) => {
    if (!files[0]) return
    setUploading(true)
    try {
      await floorsApi.uploadPlan(Number(floorId), files[0])
      setPlanUrl(URL.createObjectURL(files[0]))
      toast.success('Floor plan uploaded')
    } catch {
      toast.error('Upload failed')
    } finally {
      setUploading(false)
    }
  }, [floorId])

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop, accept: { 'image/*': [], 'application/pdf': [] }, maxFiles: 1,
  })

  if (isLoading) return <div className="flex justify-center py-16"><Spinner size={32}/></div>

  return (
    <div className="space-y-6 animate-slide-up">
      <div className="flex items-center gap-3">
        <button onClick={() => navigate(-1)} className="btn-ghost p-2"><ArrowLeft size={18}/></button>
        <div>
          <h2 className="font-display font-bold text-2xl text-white">Floor Plan — Level {floor?.levelLabel}</h2>
          <p className="text-slate-400 text-sm">Upload and annotate the floor plan</p>
        </div>
      </div>

      {planUrl ? (
        <div className="card p-2 overflow-hidden">
          <img src={planUrl} alt="Floor plan" className="w-full rounded-xl object-contain max-h-[60vh]" />
        </div>
      ) : (
        <div
          {...getRootProps()}
          className={clsx(
            'card p-16 flex flex-col items-center justify-center gap-4 cursor-pointer border-2 border-dashed transition-all duration-200',
            isDragActive ? 'border-brand-500 bg-brand-600/10' : 'border-surface-border hover:border-brand-700/60 hover:bg-surface-muted/30'
          )}
        >
          <input {...getInputProps()} />
          <div className="p-4 bg-surface-muted rounded-2xl">
            {uploading ? <Spinner size={32}/> : <Upload size={32} className="text-slate-400"/>}
          </div>
          <div className="text-center">
            <p className="font-medium text-white">{isDragActive ? 'Drop file here' : 'Upload Floor Plan'}</p>
            <p className="text-sm text-slate-500 mt-1">Drag & drop or click — PNG, JPG, or PDF</p>
          </div>
        </div>
      )}

      <div className="card p-5">
        <h3 className="font-medium text-white mb-3 flex items-center gap-2">
          <Layers size={16} className="text-sky-400"/> Floor Details
        </h3>
        <dl className="grid grid-cols-3 gap-4">
          {[
            ['Level', floor?.levelLabel],
            ['Elevation', `${floor?.elevationM ?? 0}m`],
            ['Scale', floor?.scaleRatio || '—'],
          ].map(([k, v]) => (
            <div key={k}>
              <dt className="text-xs text-slate-500 uppercase">{k}</dt>
              <dd className="text-sm text-slate-200 mt-0.5 font-mono">{v}</dd>
            </div>
          ))}
        </dl>
      </div>
    </div>
  )
}
