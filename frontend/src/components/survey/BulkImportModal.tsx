import { useState, useCallback } from 'react'
import { useDropzone } from 'react-dropzone'
import { importApi } from '@/api/import'
import Modal from '@/components/common/Modal'
import Spinner from '@/components/common/Spinner'
import { Upload, CheckCircle, FileText, AlertCircle } from 'lucide-react'
import toast from 'react-hot-toast'
import clsx from 'clsx'

interface Props {
  open: boolean
  floorId: number
  onClose: () => void
  onSuccess: () => void
}

type Step = 'upload' | 'preview' | 'done'

export default function BulkImportModal({ open, floorId, onClose, onSuccess }: Props) {
  const [step, setStep] = useState<Step>('upload')
  const [file, setFile] = useState<File | null>(null)
  const [preview, setPreview] = useState<Record<string, string>[]>([])
  const [result, setResult] = useState<{ imported: number } | null>(null)
  const [loading, setLoading] = useState(false)

  const reset = () => { setStep('upload'); setFile(null); setPreview([]); setResult(null) }

  const onDrop = useCallback(async (files: File[]) => {
    const f = files[0]
    if (!f) return
    setFile(f)
    setLoading(true)
    try {
      const rows = await importApi.previewCsv(f)
      setPreview(rows)
      setStep('preview')
    } catch {
      toast.error('Failed to parse file')
    } finally {
      setLoading(false)
    }
  }, [])

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'text/csv': ['.csv'],
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': ['.xlsx'],
    },
    maxFiles: 1,
  })

  const handleImport = async () => {
    if (!file) return
    setLoading(true)
    try {
      const res = await importApi.bulkImportSpaces(floorId, file)
      setResult(res)
      setStep('done')
      onSuccess()
      toast.success(`Imported ${res.imported} spaces successfully`)
    } catch {
      toast.error('Import failed')
    } finally {
      setLoading(false)
    }
  }

  const handleClose = () => { reset(); onClose() }

  return (
    <Modal title="Bulk Import Spaces" open={open} onClose={handleClose} size="lg">
      {step === 'upload' && (
        <div className="space-y-4">
          <p className="text-sm text-slate-400">
            Upload a CSV or XLSX file with columns: <code className="text-brand-300 font-mono text-xs">name, type, area_sq_m, notes</code>
          </p>
          <div
            {...getRootProps()}
            className={clsx(
              'border-2 border-dashed rounded-xl p-10 flex flex-col items-center gap-3 cursor-pointer transition-all duration-200',
              isDragActive
                ? 'border-brand-500 bg-brand-600/10'
                : 'border-surface-border hover:border-brand-700/60 hover:bg-surface-muted/20'
            )}
          >
            <input {...getInputProps()} />
            {loading ? <Spinner size={32}/> : <Upload size={32} className="text-slate-500"/>}
            <div className="text-center">
              <p className="text-slate-300 font-medium">{isDragActive ? 'Drop here' : 'Drop CSV or XLSX'}</p>
              <p className="text-xs text-slate-500 mt-1">or click to browse</p>
            </div>
          </div>
          <a
            href="data:text/csv;charset=utf-8,name,type,area_sq_m,notes%0AServer Room 1,SERVER_ROOM,25,Main comms room%0AApartment 101,APARTMENT,65,"
            download="spaces-template.csv"
            className="btn-ghost text-sm border border-surface-border w-full justify-center"
          >
            <FileText size={14}/> Download template CSV
          </a>
        </div>
      )}

      {step === 'preview' && (
        <div className="space-y-4">
          <div className="flex items-center gap-2 text-sm text-slate-400">
            <FileText size={14}/> {file?.name} — showing first {preview.length} rows
          </div>
          <div className="overflow-x-auto rounded-xl border border-surface-border">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-surface-border bg-surface-muted">
                  {preview[0] && Object.keys(preview[0]).map(k => (
                    <th key={k} className="text-left px-3 py-2.5 text-xs text-slate-400 uppercase font-medium">{k}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {preview.map((row, i) => (
                  <tr key={i} className="border-b border-surface-border/50 hover:bg-surface-muted/30">
                    {Object.values(row).map((v, j) => (
                      <td key={j} className="px-3 py-2 text-slate-300 font-mono text-xs">{v || '—'}</td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className="flex justify-end gap-3">
            <button onClick={reset} className="btn-ghost">← Re-upload</button>
            <button onClick={handleImport} disabled={loading} className="btn-primary">
              {loading ? <><Spinner size={14}/> Importing…</> : `Import ${preview.length}+ spaces`}
            </button>
          </div>
        </div>
      )}

      {step === 'done' && (
        <div className="flex flex-col items-center gap-4 py-8">
          <div className="p-4 bg-green-600/15 rounded-2xl">
            <CheckCircle size={36} className="text-green-400"/>
          </div>
          <div className="text-center">
            <p className="font-semibold text-white text-lg">Import Complete</p>
            <p className="text-slate-400 text-sm mt-1">{result?.imported} spaces imported into this floor</p>
          </div>
          <button onClick={handleClose} className="btn-primary mt-2">Done</button>
        </div>
      )}
    </Modal>
  )
}
