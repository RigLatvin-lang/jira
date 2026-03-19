import client from './client'
import type {
  TaskRequest,
  TaskResponse,
  StatusRequest,
  ActualTimeRequest,
  TaskStatus,
  TaskPriority,
} from '@/types'

export interface TaskFilters {
  status?: TaskStatus
  priority?: TaskPriority
  assigneeId?: string
}

export const tasksApi = {
  getAll(filters?: TaskFilters) {
    return client.get<TaskResponse[]>('/tasks', { params: filters })
  },

  getById(id: string) {
    return client.get<TaskResponse>(`/tasks/${id}`)
  },

  create(data: TaskRequest) {
    return client.post<TaskResponse>('/tasks', data)
  },

  update(id: string, data: TaskRequest) {
    return client.put<TaskResponse>(`/tasks/${id}`, data)
  },

  delete(id: string) {
    return client.delete(`/tasks/${id}`)
  },

  changeStatus(id: string, data: StatusRequest) {
    return client.patch<TaskResponse>(`/tasks/${id}/status`, data)
  },

  logActualTime(id: string, data: ActualTimeRequest) {
    return client.patch<TaskResponse>(`/tasks/${id}/actual-time`, data)
  },
}
