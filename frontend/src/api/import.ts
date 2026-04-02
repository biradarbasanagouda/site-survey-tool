import api from './client'

export const importApi = {
  bulkImportSpaces: (floorId: number, file: File) => {
    const fd = new FormData()
    fd.append('file', file)
    return api.post(`/import/spaces/${floorId}`, fd, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }).then(r => r.data as { message: string; imported: number; floorId: number })
  },

  previewCsv: (file: File) => {
    const fd = new FormData()
    fd.append('file', file)
    return api.post<Record<string, string>[]>('/import/preview/csv', fd, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }).then(r => r.data)
  },
}
