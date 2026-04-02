import api from './client'
import type { ChecklistTemplate, ChecklistResponse } from '@/types'

export const checklistsApi = {
  getTemplates: (orgId: number, scope?: string) =>
    api.get<ChecklistTemplate[]>('/checklists/templates', { params: { orgId, scope } }).then(r => r.data),

  getTemplate: (id: number) =>
    api.get<ChecklistTemplate>(`/checklists/templates/${id}`).then(r => r.data),

  submitResponse: (data: {
    templateId: number
    targetType: string
    targetId: number
    answersJson?: string
    submit: boolean
  }) => api.post<ChecklistResponse>('/checklists/responses', data).then(r => r.data),

  getResponses: (targetType: string, targetId: number) =>
    api.get<ChecklistResponse[]>('/checklists/responses', { params: { targetType, targetId } }).then(r => r.data),
}
