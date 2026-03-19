import client from './client'
import type { LoginRequest, RegisterRequest, AuthResponse } from '@/types'

export const authApi = {
  login(data: LoginRequest) {
    return client.post<AuthResponse>('/auth/login', data)
  },

  register(data: RegisterRequest) {
    return client.post<AuthResponse>('/auth/register', data)
  },
}
