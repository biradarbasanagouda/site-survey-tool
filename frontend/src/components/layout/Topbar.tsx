import { useLocation } from 'react-router-dom'
import { Bell } from 'lucide-react'

const titles: Record<string, string> = {
  '/dashboard':  'Dashboard',
  '/properties': 'Properties',
  '/checklists': 'Checklists',
  '/rf-scans':   'RF Scans',
  '/reports':    'Reports',
}

export default function Topbar() {
  const { pathname } = useLocation()
  const match = Object.entries(titles).find(([k]) => pathname.startsWith(k))
  const title = match?.[1] ?? 'Site Survey'

  return (
    <header className="h-16 flex items-center justify-between px-6 border-b border-surface-border bg-surface-card shrink-0">
      <h1 className="font-display font-semibold text-lg text-white">{title}</h1>
      <div className="flex items-center gap-3">
        <button className="relative btn-ghost p-2">
          <Bell size={18} />
          <span className="absolute top-1.5 right-1.5 w-1.5 h-1.5 bg-brand-500 rounded-full" />
        </button>
      </div>
    </header>
  )
}
