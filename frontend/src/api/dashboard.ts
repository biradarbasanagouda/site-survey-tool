import api from './client'
import type { DashboardStats } from '@/types'

export const dashboardApi = {
  getStats: (orgId: number) =>
    api.get<DashboardStats>('/dashboard', { params: { orgId } }).then(r => r.data),
}
