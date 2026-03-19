import { ref, reactive } from 'vue'
import { useTasksStore } from '@/stores/tasks'
import type { TaskPriority, TaskResponse } from '@/types'

export function useTaskForm(onSuccess?: () => void) {
  const tasksStore = useTasksStore()
  const loading = ref(false)
  const error = ref<string | null>(null)

  const form = reactive({
    title: '',
    description: '',
    assigneeId: '',
    priority: 'MEDIUM' as TaskPriority,
    estimateMinutes: null as number | null,
  })

  function resetForm() {
    form.title = ''
    form.description = ''
    form.assigneeId = ''
    form.priority = 'MEDIUM'
    form.estimateMinutes = null
  }

  function fillForm(task: TaskResponse) {
    form.title = task.title
    form.description = task.description || ''
    form.assigneeId = task.assigneeId || ''
    form.priority = task.priority
    form.estimateMinutes = task.estimateMinutes
  }

  async function submit(editId?: string) {
    if (!form.title.trim()) {
      error.value = 'Название обязательно'
      return
    }

    loading.value = true
    error.value = null
    try {
      const payload = {
        title: form.title.trim(),
        description: form.description.trim() || undefined,
        assigneeId: form.assigneeId || undefined,
        priority: form.priority,
        estimateMinutes: form.estimateMinutes ?? undefined,
      }

      if (editId) {
        await tasksStore.updateTask(editId, payload)
      } else {
        await tasksStore.createTask(payload)
      }

      resetForm()
      onSuccess?.()
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Ошибка сохранения'
    } finally {
      loading.value = false
    }
  }

  return { form, loading, error, submit, resetForm, fillForm }
}
