<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useAuth } from '@/composables/useAuth'
import BaseButton from '@/components/ui/BaseButton.vue'

const authStore = useAuthStore()
const { logout } = useAuth()
</script>

<template>
  <header class="header">
    <div class="header__left">
      <div class="header__logo">
        <svg width="28" height="28" viewBox="0 0 28 28" fill="none">
          <rect width="28" height="28" rx="8" fill="#f97316"/>
          <path d="M8 10h12M8 14h8M8 18h10" stroke="white" stroke-width="2" stroke-linecap="round"/>
        </svg>
        <span class="header__brand">TaskFlow</span>
      </div>
    </div>
    <div class="header__right">
      <div class="header__user">
        <div class="header__avatar">{{ authStore.username?.[0]?.toUpperCase() }}</div>
        <span class="header__username">{{ authStore.username }}</span>
      </div>
      <BaseButton variant="ghost" size="sm" @click="logout">Выйти</BaseButton>
    </div>
  </header>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 24px;
  background: white;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header__left {
  display: flex;
  align-items: center;
  gap: 24px;
}

.header__logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header__brand {
  font-size: 18px;
  font-weight: 700;
  color: #1f2937;
}

.header__right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header__user {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header__avatar {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
}

.header__username {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}
</style>
