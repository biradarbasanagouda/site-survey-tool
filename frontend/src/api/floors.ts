import api from './client'
import type { Floor } from '@/types'

export const floorsApi = {
  listByBuilding: (buildingId: number) =>
    api.get<Floor[]>('/floors', { params: { buildingId } }).then(r => r.data),

  get: (id: number) =>
    api.get<Floor>(`/floors/${id}`).then(r => r.data),

  create: (data: Partial<Floor>) =>
    api.post<Floor>('/floors', data).then(r => r.data),

  delete: (id: number) =>
    api.delete(`/floors/${id}`),

  uploadPlan: (id: number, file: File) => {
    const fd = new FormData()
    fd.append('file', file)
    return api.post(`/floors/${id}/plan`, fd, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }).then(r => r.data)
  },
}
