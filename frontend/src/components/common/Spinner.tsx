export default function Spinner({ size = 20 }: { size?: number }) {
  return (
    <div
      style={{ width: size, height: size }}
      className="border-2 border-surface-border border-t-brand-500 rounded-full animate-spin"
    />
  )
}
