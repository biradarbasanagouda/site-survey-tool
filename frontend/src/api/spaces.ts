import api from './client'
import type { Space } from '@/types'

export const spacesApi = {
  listByFloor: (floorId: number) =>
    api.get<Space[]>('/spaces', { params: { floorId } }).then(r => r.data),

  get: (id: number) =>
    api.get<Space>(`/spaces/${id}`).then(r => r.data),

  create: (data: Partial<Space>) =>
    api.post<Space>('/spaces', data).then(r => r.data),

  update: (id: number, data: Partial<Space>) =>
    api.put<Space>(`/spaces/${id}`, data).then(r => r.data),

  delete: (id: number) =>
    api.delete(`/spaces/${id}`),
}
