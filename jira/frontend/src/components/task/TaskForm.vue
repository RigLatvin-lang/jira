<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { storeToRefs } from 'pinia'
import { useTaskForm } from '@/composables/useTaskForm'
import { useTasksStore } from '@/stores/tasks'
import { PRIORITY_LABELS } from '@/types'
import type { TaskResponse } from '@/types'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseTextarea from '@/components/ui/BaseTextarea.vue'
import BaseSelect from '@/components/ui/BaseSelect.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const props = defineProps<{
  task?: TaskResponse
}>()

const emit = defineEmits<{
  saved: []
  cancel: []
}>()

const { form, loading, error, submit, fillForm } = useTaskForm(() => emit('saved'))

const tasksStore = useTasksStore()
const { knownUsers } = storeToRefs(tasksStore)

const priorityOptions = Object.entries(PRIORITY_LABELS).map(([value, label]) => ({ value, label }))

const assigneeOptions = computed(() => [
  { value: '', label: 'Не назначен' },
  ...knownUsers.value.map((u) => ({ value: u.id, label: u.username })),
])

onMounted(() => {
  if (props.task) fillForm(props.task)
})

function onSubmit() {
  submit(props.task?.id)
}
</script>

<template>
  <form class="task-form" @submit.prevent="onSubmit">
    <BaseInput
      v-model="form.title"
      label="Название"
      placeholder="Введите название задачи"
    />
    <BaseTextarea
      v-model="form.description"
      label="Описание"
      placeholder="Опишите задачу подробнее..."
      :rows="4"
    />
    <BaseSelect
      v-model="form.assigneeId"
      label="Исполнитель"
      :options="assigneeOptions"
    />
    <div class="task-form__row">
      <BaseSelect
        v-model="form.priority"
        label="Приоритет"
        :options="priorityOptions"
      />
      <BaseInput
        v-model="form.estimateMinutes"
        label="Оценка (мин)"
        type="number"
        placeholder="0"
      />
    </div>
    <p v-if="error" class="task-form__error">{{ error }}</p>
    <div class="task-form__actions">
      <BaseButton variant="secondary" type="button" @click="emit('cancel')">Отмена</BaseButton>
      <BaseButton :loading="loading" :disabled="!form.title.trim()">
        {{ task ? 'Сохранить' : 'Создать' }}
      </BaseButton>
    </div>
  </form>
</template>

<style scoped>
.task-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.task-form__row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.task-form__error {
  color: #ef4444;
  font-size: 13px;
  margin: 0;
  padding: 8px 12px;
  background: #fef2f2;
  border-radius: 8px;
}

.task-form__actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding-top: 4px;
}
</style>
