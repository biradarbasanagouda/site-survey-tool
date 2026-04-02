import api from './client'
import type { Property, PageResponse } from '@/types'

export const propertiesApi = {
  list: (orgId: number, page = 0, size = 20) =>
    api.get<PageResponse<Property>>('/properties', { params: { orgId, page, size } }).then(r => r.data),

  get: (id: number) =>
    api.get<Property>(`/properties/${id}`).then(r => r.data),

  create: (data: Partial<Property>) =>
    api.post<Property>('/properties', data).then(r => r.data),

  update: (id: number, data: Partial<Property>) =>
    api.put<Property>(`/properties/${id}`, data).then(r => r.data),

  delete: (id: number) =>
    api.delete(`/properties/${id}`),
}
