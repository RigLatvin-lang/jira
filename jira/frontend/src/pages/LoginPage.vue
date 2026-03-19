<script setup lang="ts">
import { ref } from 'vue'
import { useAuth } from '@/composables/useAuth'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const { login, loading, error } = useAuth()

const username = ref('')
const password = ref('')

function onSubmit() {
  login(username.value, password.value)
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="auth-logo">
        <svg width="40" height="40" viewBox="0 0 28 28" fill="none">
          <rect width="28" height="28" rx="8" fill="#f97316"/>
          <path d="M8 10h12M8 14h8M8 18h10" stroke="white" stroke-width="2" stroke-linecap="round"/>
        </svg>
        <h1 class="auth-title">TaskFlow</h1>
      </div>
      <p class="auth-subtitle">Войдите в свой аккаунт</p>

      <form class="auth-form" @submit.prevent="onSubmit">
        <BaseInput
          v-model="username"
          label="Имя пользователя"
          placeholder="Введите имя пользователя"
        />
        <BaseInput
          v-model="password"
          label="Пароль"
          type="password"
          placeholder="Введите пароль"
        />
        <p v-if="error" class="auth-error">{{ error }}</p>
        <BaseButton size="lg" :loading="loading" :disabled="!username || !password" style="width: 100%">
          Войти
        </BaseButton>
      </form>

      <p class="auth-link">
        Нет аккаунта?
        <RouterLink to="/register">Зарегистрироваться</RouterLink>
      </p>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
  padding: 20px;
}

.auth-card {
  background: white;
  border-radius: 20px;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
}

.auth-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.auth-title {
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.auth-subtitle {
  color: #6b7280;
  font-size: 14px;
  margin: 0 0 28px;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.auth-error {
  color: #ef4444;
  font-size: 13px;
  margin: 0;
  padding: 8px 12px;
  background: #fef2f2;
  border-radius: 8px;
}

.auth-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #6b7280;
}

.auth-link a {
  color: #f97316;
  text-decoration: none;
  font-weight: 500;
}

.auth-link a:hover {
  text-decoration: underline;
}
</style>
