<template>
    <a-layout>
        <a-layout-sider width="200" style="background: #fff">
            <a-menu
                    mode="inline"
                    :style="{ height: '100%', borderRight: 0 }"
            >
                <a-sub-menu key="sub1">
                    <template #title>
                <span>
                  <user-outlined/>
                  subnav 1
                </span>
                    </template>
                    <a-menu-item key="1">option1</a-menu-item>
                    <a-menu-item key="2">option2</a-menu-item>
                    <a-menu-item key="3">option3</a-menu-item>
                    <a-menu-item key="4">option4</a-menu-item>
                </a-sub-menu>
                <a-sub-menu key="sub2">
                    <template #title>
                <span>
                  <laptop-outlined/>
                  subnav 2
                </span>
                    </template>
                    <a-menu-item key="5">option5</a-menu-item>
                    <a-menu-item key="6">option6</a-menu-item>
                    <a-menu-item key="7">option7</a-menu-item>
                    <a-menu-item key="8">option8</a-menu-item>
                </a-sub-menu>
                <a-sub-menu key="sub3">
                    <template #title>
                <span>
                  <notification-outlined/>
                  subnav 3
                </span>
                    </template>
                    <a-menu-item key="9">option9</a-menu-item>
                    <a-menu-item key="10">option10</a-menu-item>
                    <a-menu-item key="11">option11</a-menu-item>
                    <a-menu-item key="12">option12</a-menu-item>
                </a-sub-menu>
            </a-menu>
        </a-layout-sider>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <pre>{{ ebooks }}{{ ebooks2 }}</pre><!--pre标签就是会把里面长什么样含空格,原封不动的全部给你展示到页面上-->
        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRef,} from 'vue';
//导入集成HTTP库Axios
import axios from 'axios';

export default defineComponent({
    name: 'HomeView',
    // setup() 初始化化方法 Vue2.0中的data()、mounted()、methods()被setup()取代
    // (response)=>{}定义函数 参数response
    // 初始化的逻辑都写到onMountted方法里，setup放一些参数定义、方法定义
    setup() {
        console.log("setup");
        /*Vue3数据绑定显示列表数据的两种方式ref，reactive*/
        const ebooks = ref();//Vue3新增了ref用来定义响应式数据（实时相应
        //Vue3新增的reactive,books是自己定义的属性用来放电子书列表
        //{books: []}是一个Json对象 ，要把查出来的对象往里面放所以是一个空数组
        const ebooks1 = reactive({books: []})

        onMounted(() => {
            console.log("onMounted");
            axios.get("http://localhost:8880/ebook/list?name=Python").then(
                (response) => {
                    const date = response.data; //获取response里面data数据（commonResp数据
                    ebooks.value = date.content; //用ebooks.value赋值，不用ebooks
                    ebooks1.books = date.content;
                    console.log(response);
                }
            );
        });

        //返回数据
        return {
            ebooks,
            ebooks2:toRef(ebooks1,"books")  //toRef把books变成响应式变量,定义变量名ebooks2
        }
    }
});
</script>
