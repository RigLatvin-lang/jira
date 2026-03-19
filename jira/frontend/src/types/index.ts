export type TaskStatus = 'WAITING' | 'ANALYSIS' | 'DEVELOPMENT' | 'TESTING' | 'DONE'

export type TaskPriority = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW'

export interface TaskResponse {
  id: string
  title: string
  description: string | null
  creatorId: string
  creatorUsername: string
  assigneeId: string | null
  assigneeUsername: string | null
  status: TaskStatus
  priority: TaskPriority
  estimateMinutes: number | null
  actualMinutes: number | null
  createdAt: string
  updatedAt: string
}

export interface TaskRequest {
  title: string
  description?: string
  assigneeId?: string
  priority?: TaskPriority
  estimateMinutes?: number
}

export interface StatusRequest {
  status: TaskStatus
}

export interface ActualTimeRequest {
  minutes: number
}

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
}

export interface AuthResponse {
  token: string
  username: string
}

export const TASK_STATUSES: TaskStatus[] = ['WAITING', 'ANALYSIS', 'DEVELOPMENT', 'TESTING', 'DONE']

export const STATUS_LABELS: Record<TaskStatus, string> = {
  WAITING: 'Ожидание',
  ANALYSIS: 'Анализ',
  DEVELOPMENT: 'Разработка',
  TESTING: 'Тестирование',
  DONE: 'Готово',
}

export const PRIORITY_LABELS: Record<TaskPriority, string> = {
  CRITICAL: 'Критический',
  HIGH: 'Высокий',
  MEDIUM: 'Средний',
  LOW: 'Низкий',
}

export const PRIORITY_COLORS: Record<TaskPriority, string> = {
  CRITICAL: '#ef4444',
  HIGH: '#f97316',
  MEDIUM: '#eab308',
  LOW: '#22c55e',
}
