interface PageHeaderProps {
  title: string
  description?: string
  action?: React.ReactNode
}

export default function PageHeader({ title, description, action }: PageHeaderProps) {
  return (
    <div className="flex items-center justify-between mb-6">
      <div>
        <h2 className="font-display font-semibold text-2xl text-white">{title}</h2>
        {description && <p className="text-slate-400 text-sm mt-1">{description}</p>}
      </div>
      {action}
    </div>
  )
}
