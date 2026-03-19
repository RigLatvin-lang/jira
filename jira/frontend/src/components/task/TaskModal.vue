<script setup lang="ts">
import { ref } from 'vue'
import type { TaskResponse } from '@/types'
import BaseModal from '@/components/ui/BaseModal.vue'
import TaskForm from './TaskForm.vue'
import TaskDetails from './TaskDetails.vue'

const props = defineProps<{
  task?: TaskResponse
}>()

const emit = defineEmits<{
  close: []
  saved: []
  deleted: []
}>()

const isEditing = ref(!props.task)

function onEdit() {
  isEditing.value = true
}

function onSaved() {
  emit('saved')
}

function onCancel() {
  if (props.task) {
    isEditing.value = false
  } else {
    emit('close')
  }
}
</script>

<template>
  <BaseModal
    :title="isEditing ? (task ? 'Редактирование' : 'Новая задача') : undefined"
    @close="emit('close')"
  >
    <TaskForm
      v-if="isEditing"
      :task="task"
      @saved="onSaved"
      @cancel="onCancel"
    />
    <TaskDetails
      v-else-if="task"
      :task="task"
      @edit="onEdit"
      @deleted="emit('deleted')"
    />
  </BaseModal>
</template>
