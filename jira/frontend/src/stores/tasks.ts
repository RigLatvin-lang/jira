import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { tasksApi } from '@/api/tasks'
import { useAuthStore } from '@/stores/auth'
import type { TaskResponse, TaskStatus, TaskPriority } from '@/types'

export interface KnownUser {
  id: string
  username: string
}

export const useTasksStore = defineStore('tasks', () => {
  const tasks = ref<TaskResponse[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const filterMyTasks = ref(false)

  const knownUsers = computed(() => {
    const map = new Map<string, string>()
    for (const task of tasks.value) {
      map.set(task.creatorId, task.creatorUsername)
      if (task.assigneeId && task.assigneeUsername) {
        map.set(task.assigneeId, task.assigneeUsername)
      }
    }
    return Array.from(map, ([id, username]) => ({ id, username }))
  })

  const currentUserId = computed(() => {
    const auth = useAuthStore()
    const user = knownUsers.value.find((u) => u.username === auth.username)
    return user?.id ?? null
  })

  const filteredTasks = computed(() => {
    if (!filterMyTasks.value) return tasks.value
    const auth = useAuthStore()
    return tasks.value.filter(
      (t) => t.creatorUsername === auth.username || t.assigneeUsername === auth.username,
    )
  })

  const tasksByStatus = computed(() => {
    const map: Record<TaskStatus, TaskResponse[]> = {
      WAITING: [],
      ANALYSIS: [],
      DEVELOPMENT: [],
      TESTING: [],
      DONE: [],
    }
    for (const task of filteredTasks.value) {
      map[task.status].push(task)
    }
    return map
  })

  async function fetchTasks(filters?: { status?: TaskStatus; priority?: TaskPriority; assigneeId?: string }) {
    loading.value = true
    error.value = null
    try {
      const { data } = await tasksApi.getAll(filters)
      tasks.value = data
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Ошибка загрузки задач'
    } finally {
      loading.value = false
    }
  }

  async function createTask(data: { title: string; description?: string; assigneeId?: string; priority?: TaskPriority; estimateMinutes?: number }) {
    const { data: task } = await tasksApi.create(data)
    tasks.value.push(task)
    return task
  }

  async function updateTask(id: string, data: { title: string; description?: string; assigneeId?: string; priority?: TaskPriority; estimateMinutes?: number }) {
    const { data: task } = await tasksApi.update(id, data)
    const idx = tasks.value.findIndex((t) => t.id === id)
    if (idx !== -1) tasks.value[idx] = task
    return task
  }

  async function deleteTask(id: string) {
    await tasksApi.delete(id)
    tasks.value = tasks.value.filter((t) => t.id !== id)
  }

  async function changeStatus(id: string, status: TaskStatus) {
    const { data: task } = await tasksApi.changeStatus(id, { status })
    const idx = tasks.value.findIndex((t) => t.id === id)
    if (idx !== -1) tasks.value[idx] = task
    return task
  }

  async function logTime(id: string, minutes: number) {
    const { data: task } = await tasksApi.logActualTime(id, { minutes })
    const idx = tasks.value.findIndex((t) => t.id === id)
    if (idx !== -1) tasks.value[idx] = task
    return task
  }

  function updateTaskLocally(id: string, status: TaskStatus) {
    const idx = tasks.value.findIndex((t) => t.id === id)
    if (idx !== -1) {
      tasks.value[idx] = { ...tasks.value[idx]!, status }
    }
  }

  return {
    tasks,
    loading,
    error,
    filterMyTasks,
    knownUsers,
    currentUserId,
    tasksByStatus,
    fetchTasks,
    createTask,
    updateTask,
    deleteTask,
    changeStatus,
    logTime,
    updateTaskLocally,
  }
})
