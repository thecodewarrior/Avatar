import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import {TippyPlugin} from "tippy.vue";

const app = createApp(App)

app.use(router)
app.use(TippyPlugin)

app.mount('#app')
