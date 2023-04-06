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

/**
 * 多环境配置文件要放在web根目录下
 * .env.xxx,后缀xxx和package.json里的指令的 -mode xxx对应
 * */
//打印日志环境
console.log('环境：',process.env.NODE_ENV);
console.log('服务端：',process.env.VUE_APP_SERVER);
