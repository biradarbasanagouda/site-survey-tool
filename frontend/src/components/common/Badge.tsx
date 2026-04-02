import clsx from 'clsx'

type BadgeVariant = 'blue' | 'green' | 'yellow' | 'red' | 'slate'

const variants: Record<BadgeVariant, string> = {
  blue:   'bg-brand-600/20 text-brand-300 border-brand-600/30',
  green:  'bg-emerald-600/20 text-emerald-300 border-emerald-600/30',
  yellow: 'bg-amber-600/20 text-amber-300 border-amber-600/30',
  red:    'bg-red-600/20 text-red-300 border-red-600/30',
  slate:  'bg-slate-600/20 text-slate-300 border-slate-600/30',
}

export default function Badge({ label, variant = 'slate' }: { label: string; variant?: BadgeVariant }) {
  return (
    <span className={clsx('badge border', variants[variant])}>{label}</span>
  )
}
