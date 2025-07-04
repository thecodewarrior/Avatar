import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PrebuiltFilesView from "@/views/PrebuiltFilesView.vue";
import AnimatedBlackholeView from "@/views/AnimatedBlackholeView.vue";

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: {
        title: "thecodewarrior's avatars"
      },
    },
    {
      path: '/blackhole',
      name: 'blackhole',
      component: AnimatedBlackholeView,
      meta: {
        title: "Black hole avatar"
      },
    },
    {
      path: '/prebuilt',
      name: 'prebuilt',
      component: PrebuiltFilesView,
      meta: {
        title: "Prebuilt files"
      },
    },
  ],
})

export default router
