import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Radio, Eye, EyeOff } from 'lucide-react'
import { useAuthStore } from '@/store/authStore'
import { authApi } from '@/api/auth'
import toast from 'react-hot-toast'

const schema = z.object({
  email: z.string().email('Invalid email'),
  password: z.string().min(6, 'Min 6 characters'),
})
type FormData = z.infer<typeof schema>

export default function LoginPage() {
  const navigate = useNavigate()
  const setAuth = useAuthStore(s => s.setAuth)
  const [showPw, setShowPw] = useState(false)
  const [loading, setLoading] = useState(false)

  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: { email: 'admin@isp.com', password: 'Admin@123' }
  })

  const onSubmit = async (data: FormData) => {
    setLoading(true)
    try {
      const res = await authApi.login(data.email, data.password)
      setAuth(res.user, res.accessToken, res.refreshToken)
      toast.success(`Welcome back, ${res.user.fullName}!`)
      navigate('/dashboard')
    } catch {
      toast.error('Invalid credentials. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-surface flex items-center justify-center p-4"
         style={{ backgroundImage: 'radial-gradient(ellipse 80% 50% at 50% -20%, rgba(43,135,251,0.12), transparent)' }}>
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="flex flex-col items-center mb-10 gap-3">
          <div className="w-14 h-14 bg-brand-600 rounded-2xl flex items-center justify-center shadow-glow">
            <Radio size={28} className="text-white" />
          </div>
          <div className="text-center">
            <h1 className="font-display font-bold text-3xl text-white">SiteSurvey</h1>
            <p className="text-slate-400 text-sm mt-1">ISP Network Planning Platform</p>
          </div>
        </div>

        {/* Card */}
        <div className="card p-8">
          <h2 className="font-display font-semibold text-xl text-white mb-6">Sign in to your account</h2>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label className="label">Email address</label>
              <input {...register('email')} type="email" className="input" placeholder="you@isp.com" />
              {errors.email && <p className="text-red-400 text-xs mt-1">{errors.email.message}</p>}
            </div>
            <div>
              <label className="label">Password</label>
              <div className="relative">
                <input {...register('password')} type={showPw ? 'text' : 'password'} className="input pr-10" />
                <button type="button" onClick={() => setShowPw(!showPw)}
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-500 hover:text-slate-300 transition-colors">
                  {showPw ? <EyeOff size={16}/> : <Eye size={16}/>}
                </button>
              </div>
              {errors.password && <p className="text-red-400 text-xs mt-1">{errors.password.message}</p>}
            </div>
            <button type="submit" disabled={loading} className="btn-primary w-full justify-center mt-2 py-3">
              {loading ? 'Signing in…' : 'Sign in'}
            </button>
          </form>

          <div className="mt-6 p-4 bg-surface-muted rounded-xl border border-surface-border">
            <p className="text-xs text-slate-500 font-mono">
              Demo credentials<br/>
              <span className="text-slate-300">admin@isp.com / Admin@123</span>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}
