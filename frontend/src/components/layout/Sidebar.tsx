import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard, MapPin, Building2, Layers, Radio,
  ClipboardCheck, FileBarChart2, Signal, LogOut
} from 'lucide-react'
import { useAuthStore } from '@/store/authStore'
import clsx from 'clsx'

const nav = [
  { to: '/dashboard',  label: 'Dashboard',    icon: LayoutDashboard },
  { to: '/properties', label: 'Properties',   icon: MapPin },
  { to: '/checklists', label: 'Checklists',   icon: ClipboardCheck },
  { to: '/rf-scans',   label: 'RF Scans',     icon: Signal },
  { to: '/reports',    label: 'Reports',      icon: FileBarChart2 },
]

export default function Sidebar() {
  const logout = useAuthStore(s => s.logout)
  const user   = useAuthStore(s => s.user)

  return (
    <aside className="w-64 flex flex-col bg-surface-card border-r border-surface-border shrink-0">
      {/* Logo */}
      <div className="h-16 flex items-center px-6 border-b border-surface-border gap-3">
        <div className="w-8 h-8 bg-brand-600 rounded-lg flex items-center justify-center">
          <Radio size={16} className="text-white" />
        </div>
        <span className="font-display font-700 text-lg text-white tracking-tight">SiteSurvey</span>
      </div>

      {/* Nav */}
      <nav className="flex-1 p-3 space-y-0.5 overflow-y-auto">
        {nav.map(({ to, label, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            className={({ isActive }) => clsx(
              'flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all duration-150',
              isActive
                ? 'bg-brand-600/15 text-brand-400 border border-brand-600/30'
                : 'text-slate-400 hover:text-slate-100 hover:bg-surface-muted'
            )}
          >
            <Icon size={16} />
            {label}
          </NavLink>
        ))}
      </nav>

      {/* User */}
      <div className="p-3 border-t border-surface-border">
        <div className="flex items-center gap-3 px-3 py-2.5 rounded-xl">
          <div className="w-8 h-8 rounded-full bg-brand-700 flex items-center justify-center text-xs font-semibold text-brand-200">
            {user?.fullName?.charAt(0).toUpperCase() ?? 'U'}
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-medium text-slate-200 truncate">{user?.fullName}</p>
            <p className="text-xs text-slate-500 truncate">{user?.email}</p>
          </div>
          <button onClick={() => logout()} className="text-slate-500 hover:text-red-400 transition-colors">
            <LogOut size={15} />
          </button>
        </div>
      </div>
    </aside>
  )
}
