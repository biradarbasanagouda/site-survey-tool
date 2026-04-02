import api from './client'
import type { AuthResponse } from '@/types'

export const authApi = {
  login: (email: string, password: string) =>
    api.post<AuthResponse>('/auth/login', { email, password }).then(r => r.data),

  register: (email: string, fullName: string, password: string) =>
    api.post<AuthResponse>('/auth/register', { email, fullName, password }).then(r => r.data),

  logout: (refreshToken: string) =>
    api.post('/auth/logout', { refreshToken }),
}
