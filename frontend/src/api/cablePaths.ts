import api from './client'

export interface CablePathPayload {
  propertyId: number
  medium: string
  fromSpaceId?: number
  toSpaceId?: number
  lengthM?: number
  notes?: string
}

export const cablePathsApi = {
  listByProperty: (propertyId: number) =>
    api.get('/cable-paths', { params: { propertyId } }).then(r => r.data),

  create: (data: CablePathPayload) =>
    api.post('/cable-paths', data).then(r => r.data),

  delete: (id: number) => api.delete(`/cable-paths/${id}`),
}
