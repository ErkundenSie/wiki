import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//引入
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
//导入所有图标1
import * as Icons from '@ant-design/icons-vue';
const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app')
//全局使用图标2，注意const app和分段
const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}