import api from './client'
import type { Building } from '@/types'

export const buildingsApi = {
  listByProperty: (propertyId: number) =>
    api.get<Building[]>('/buildings', { params: { propertyId } }).then(r => r.data),

  get: (id: number) =>
    api.get<Building>(`/buildings/${id}`).then(r => r.data),

  create: (data: Partial<Building>) =>
    api.post<Building>('/buildings', data).then(r => r.data),

  update: (id: number, data: Partial<Building>) =>
    api.put<Building>(`/buildings/${id}`, data).then(r => r.data),

  delete: (id: number) =>
    api.delete(`/buildings/${id}`),
}
