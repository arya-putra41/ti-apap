import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SekolahMainView from '@/views/sekolah/SekolahMainView.vue'
import SekolahDetailView from '@/views/sekolah/SekolahDetailView.vue'
import SekolahCreateView from '@/views/sekolah/SekolahCreateView.vue'
import SekolahUpdateView from '@/views/sekolah/SekolahUpdateView.vue'
import RombelMainView from '@/views/rombel/RombelMainView.vue'
import RombelDetailView from '@/views/rombel/RombelDetailView.vue'
import RombelCreateView from '@/views/rombel/RombelCreateView.vue'
import RombelUpdateView from '@/views/rombel/RombelUpdateView.vue'
import PenerimaMainView from '@/views/penerima/PenerimaMainView.vue'
import PenerimaDetailView from '@/views/penerima/PenerimaDetailView.vue'
import PenerimaCreateView from '@/views/penerima/PenerimaCreateView.vue'
import PenerimaUpdateView from '@/views/penerima/PenerimaUpdateView.vue'
import PenerimaVerifyView from '@/views/penerima/PenerimaVerifyView.vue'
import PenerimaRejectView from '@/views/penerima/PenerimaRejectView.vue'
import PenerimaDeactivateView from '@/views/penerima/PenerimaDeactivateView.vue'
import PenerimaReviewView from '@/views/penerima/PenerimaReviewView.vue'
import PenerimaQueueView from '@/views/penerima/PenerimaQueueView.vue'
import PenerimaStatisticsView from '@/views/penerima/PenerimaStatisticsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/sekolah',
      name: 'sekolah',
      component: SekolahMainView,
    },
    {
      path: '/sekolah/:id',
      name: 'sekolah-detail',
      component: SekolahDetailView,
    },
    {
      path: '/sekolah/create',
      name: 'sekolah-create',
      component: SekolahCreateView,
    },
    {
      path: '/sekolah/:id/update',
      name: 'sekolah-update',
      component: SekolahUpdateView,
    },
    {
      path: '/rombel',
      name: 'rombel',
      component: RombelMainView,
    },
    {
      path: '/rombel/:id',
      name: 'rombel-detail',
      component: RombelDetailView,
    },
    {
      path: '/rombel/create',
      name: 'rombel-create',
      component: RombelCreateView,
    },
    {
      path: '/rombel/:id/update',
      name: 'rombel-update',
      component: RombelUpdateView,
    },
    {
      path: '/penerima',
      name: 'penerima',
      component: PenerimaMainView,
    },
    {
      path: '/penerima/:id',
      name: 'penerima-detail',
      component: PenerimaDetailView,
    },
    {
      path: '/penerima/create',
      name: 'penerima-create',
      component: PenerimaCreateView,
    },
    {
      path: '/penerima/:id/update',
      name: 'penerima-update',
      component: PenerimaUpdateView,
    },
    {
      path: '/penerima/:id/verifikasi',
      name: 'penerima-verifikasi',
      component: PenerimaVerifyView,
    },
    {
      path: '/penerima/:id/tolak',
      name: 'penerima-tolak',
      component: PenerimaRejectView,
    },
    {
      path: '/penerima/:id/nonaktifkan',
      name: 'penerima-nonaktifkan',
      component: PenerimaDeactivateView,
    },
    {
      path: '/penerima/:id/tinjau-ulang',
      name: 'penerima-tinjau-ulang',
      component: PenerimaReviewView,
    },
    {
      path: '/penerima/antrian-verifikasi',
      name: 'penerima-antrian-verifikasi',
      component: PenerimaQueueView,
    },
    {
      path: '/penerima/statistik',
      name: 'penerima-statistik',
      component: PenerimaStatisticsView,
    },
  ],
})

export default router
