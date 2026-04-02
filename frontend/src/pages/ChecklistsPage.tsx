import { useQuery } from '@tanstack/react-query'
import { checklistsApi } from '@/api/checklists'
import { useAuthStore } from '@/store/authStore'
import { ClipboardCheck, ChevronRight } from 'lucide-react'
import EmptyState from '@/components/common/EmptyState'
import PageHeader from '@/components/common/PageHeader'
import Spinner from '@/components/common/Spinner'
import Badge from '@/components/common/Badge'

const scopeColor: Record<string, 'blue' | 'green' | 'yellow' | 'slate'> = {
  SPACE: 'blue', BUILDING: 'green', FLOOR: 'yellow', PROPERTY: 'slate',
}

export default function ChecklistsPage() {
  const orgId = useAuthStore(s => s.orgId)

  const { data: templates, isLoading } = useQuery({
    queryKey: ['checklist-templates', orgId],
    queryFn: () => checklistsApi.getTemplates(orgId),
  })

  return (
    <div className="space-y-6 animate-slide-up">
      <PageHeader title="Checklists" description="Survey templates for capturing site information" />

      {isLoading ? (
        <div className="flex justify-center py-16"><Spinner size={32}/></div>
      ) : !templates?.length ? (
        <EmptyState icon={<ClipboardCheck size={36}/>} title="No templates found"
          description="Checklist templates are created by your organisation admin" />
      ) : (
        <div className="space-y-3">
          {templates.map((t) => (
            <div key={t.id} className="card p-5 flex items-center gap-4 hover:border-brand-700/50 transition-all cursor-pointer group">
              <div className="p-3 bg-green-600/15 rounded-xl shrink-0">
                <ClipboardCheck size={20} className="text-green-400"/>
              </div>
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2 mb-1">
                  <h3 className="font-semibold text-white truncate">{t.name}</h3>
                  <Badge label={t.scope} variant={scopeColor[t.scope] ?? 'slate'} />
                  <span className="text-xs text-slate-600 font-mono">v{t.version}</span>
                </div>
                <p className="text-sm text-slate-500">
                  {t.isActive ? '✓ Active' : '✗ Inactive'}
                </p>
              </div>
              <ChevronRight size={16} className="text-slate-600 group-hover:text-slate-300 transition-colors shrink-0"/>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
