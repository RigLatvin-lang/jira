import { ref } from 'vue'
import { useTasksStore } from '@/stores/tasks'

export function useTaskDetails() {
  const tasksStore = useTasksStore()
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function deleteTask(id: string, onSuccess?: () => void) {
    loading.value = true
    error.value = null
    try {
      await tasksStore.deleteTask(id)
      onSuccess?.()
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Ошибка удаления'
    } finally {
      loading.value = false
    }
  }

  async function logTime(id: string, minutes: number) {
    loading.value = true
    error.value = null
    try {
      await tasksStore.logTime(id, minutes)
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Ошибка сохранения времени'
    } finally {
      loading.value = false
    }
  }

  return { loading, error, deleteTask, logTime }
}
