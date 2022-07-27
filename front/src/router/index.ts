import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    redirect: '/user'
  },
  {
    path: '/user',
    name: 'User',
    component: () => import('@/views/UserManage.vue')
  },
  {
    path: '/homework',
    name: 'Homework',
    component: () => import('@/views/Homework.vue')
  },
  {
    path: '/apply',
    name: 'Apply',
    component: () => import('@/views/ApplyManage.vue')
  },
  {
    path: '/develop',
    name: 'Develop',
    component: () => import('@/views/DevelopManage.vue')
  },
  {
    path: '/cert',
    name: 'Cert',
    component: () => import('@/views/CertManage.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
