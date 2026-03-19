import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { authApi } from '@/api/auth'

export function useAuth() {
  const router = useRouter()
  const authStore = useAuthStore()
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function login(username: string, password: string) {
    loading.value = true
    error.value = null
    try {
      const { data } = await authApi.login({ username, password })
      authStore.setAuth(data.token, data.username)
      router.push('/')
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Неверный логин или пароль'
    } finally {
      loading.value = false
    }
  }

  async function register(username: string, email: string, password: string) {
    loading.value = true
    error.value = null
    try {
      const { data } = await authApi.register({ username, email, password })
      authStore.setAuth(data.token, data.username)
      router.push('/')
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Ошибка регистрации'
    } finally {
      loading.value = false
    }
  }

  function logout() {
    authStore.logout()
    router.push('/login')
  }

  return { loading, error, login, register, logout }
}
