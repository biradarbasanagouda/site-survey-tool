import { MapPin, Building2, Layers, Layout, Cpu, ClipboardCheck, FileBarChart2 } from 'lucide-react'
import StatCard from '@/components/common/StatCard'
import type { DashboardStats } from '@/types'

export default function StatsGrid({ stats }: { stats: DashboardStats }) {
  return (
    <div className="grid grid-cols-2 xl:grid-cols-4 gap-4">
      <StatCard label="Properties"     value={stats.totalProperties}          icon={<MapPin size={18}/>}          color="text-brand-400" />
      <StatCard label="Buildings"      value={stats.totalBuildings}           icon={<Building2 size={18}/>}       color="text-violet-400" />
      <StatCard label="Floors"         value={stats.totalFloors}              icon={<Layers size={18}/>}          color="text-sky-400" />
      <StatCard label="Spaces"         value={stats.totalSpaces}              icon={<Layout size={18}/>}          color="text-teal-400" />
      <StatCard label="Equipment"      value={stats.totalEquipment}           icon={<Cpu size={18}/>}             color="text-orange-400" />
      <StatCard label="Surveys"        value={stats.totalChecklistResponses}  icon={<ClipboardCheck size={18}/>}  color="text-green-400" />
      <StatCard label="Pending Reports" value={stats.pendingReports}          icon={<FileBarChart2 size={18}/>}   color="text-amber-400"
        sub="awaiting generation" />
    </div>
  )
}
