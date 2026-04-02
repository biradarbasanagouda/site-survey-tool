import api from './client'
import type { Equipment } from '@/types'

export const equipmentApi = {
  listBySpace: (spaceId: number) =>
    api.get<Equipment[]>('/equipment', { params: { spaceId } }).then(r => r.data),

  create: (data: {
    spaceId: number; type: string; model?: string; vendor?: string
    powerWatts?: number; serialNumber?: string
  }) => api.post<Equipment>('/equipment', data).then(r => r.data),

  delete: (id: number) => api.delete(`/equipment/${id}`),
}
