import api from './client'
import type { Report } from '@/types'

export const reportsApi = {
  generate: (propertyId: number) =>
    api.post<Report>('/reports/generate', { propertyId }).then(r => r.data),

  list: (propertyId: number) =>
    api.get<Report[]>('/reports', { params: { propertyId } }).then(r => r.data),

  get: (id: number) =>
    api.get<Report>(`/reports/${id}`).then(r => r.data),
}
