<template>
    <a-layout>
        <a-layout-sider width="200" style="background: #fff">
            <a-menu
                    mode="inline"
                    :style="{ height: '100%', borderRight: 0 }"
            >
                <!--添加欢迎界面-->
                <a-menu-item key="welcome">
                    <router-link to="/">
                        <MailOutline/>
                        <span>欢迎</span>
                    </router-link>
                </a-menu-item>
                <!--循环level1-->
                <a-sub-menu v-for="item in level1" :key="item.id">
                    <template v-slot:title>
                        <span>
                          <user-outlined/>{{ item.name }}
                        </span>
                    </template><!--循环children-->
                    <a-menu-item v-for="child in item.children" :key="child.id">
                        <MailOutlined/>
                        <span>{{ child.name }}</span>
                    </a-menu-item>
                </a-sub-menu>
            </a-menu>
        </a-layout-sider>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <!-- <pre>{{ ebooks }}{{ ebooks2 }}</pre>&lt;!&ndash;pre标签就是会把里面长什么样含空格,原封不动的全部给你展示到页面上&ndash;&gt;-->
            <!--gutter: 20, column: 3 间距20,3列-->
            <a-list item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3}" :data-source="ebooks">
                <!--item为一个电子书-->
                <template #renderItem="{ item }">
                    <a-list-item key="item.name">
                        <template #actions>
                          <span v-for="{ type, text } in actions" :key="type">
                            <component v-bind:is="type" style="margin-right: 8px"/>
                            {{ text }}
                          </span>
                        </template>
                        <a-list-item-meta :description="item.description">
                            <template #title>
                                <a :href="item.href">{{ item.name }}</a>
                            </template>
                            <template #avatar>
                                <a-avatar :src="item.cover"/>
                            </template><!--cover图标-->
                        </a-list-item-meta>
                    </a-list-item>
                </template>
            </a-list>

        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRef,} from 'vue';
//导入集成HTTP库Axios
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
////假数据
// const listData: any = [];
//
// for (let i = 0; i < 23; i++) {
//     listData.push({
//         href: 'https://www.antdv.com/',
//         title: `ant design vue part ${i}`,
//         avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
//         description:
//             'Ant Design, a design language for background applications, is refined by Ant UED Team.',
//         content:
//             'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
//     });
// }
export default defineComponent({
    name: 'Home',
    // setup() 初始化化方法 Vue2.0中的data()、mounted()、methods()被setup()取代
    // (response)=>{}定义函数 参数response
    // 初始化的逻辑都写到onMountted方法里，setup放一些参数定义、方法定义
    setup() {
        // console.log("setup");
        /*Vue3数据绑定显示列表数据的两种方式ref，reactive*/
        const ebooks = ref();//Vue3新增了ref用来定义响应式数据（实时相应
        //Vue3新增的reactive,books是自己定义的属性用来放电子书列表
        //{books: []}是一个Json对象 ，要把查出来的对象往里面放所以是一个空数组
        const ebooks1 = reactive({books: []})
        /**
         * handleQueryCategory 查询所有分类
         */
        let categorys: any;
        const level1 = ref();
        const handleQueryCategory = () => {
            axios.get("/category/all").then((response) => {
                const data = response.data;
                if (data.success) {
                    categorys = data.content;
                    console.log("原始数据：",categorys);

                    level1.value = [];
                    level1.value = Tool.array2Tree(categorys, 0);
                    console.log("树形结构：",level1.value)
                } else {
                    message.error(data.message);
                }
            });

        }
        //测试方法点击打印日志
        const handleClick = ()=> {
            console.log("menu click")
        };
        onMounted(() => {
            handleQueryCategory();//初始化调用加载分类
            // console.log("onMounted2222");
            // 在main.ts中修改axios默认请求地址就不用写前面的链接了
            //params:{}返回后端，1000条一页（查出全部
            axios.get("/ebook/list", {
                params: {
                    page: 1,
                    size: 1000
                }
            }).then(
                (response) => {
                    const date = response.data; //获取response里面data数据（commonResp数据
                    ebooks.value = date.content.list; //用ebooks.value赋值，不用ebooks
                    // ebooks1.books = date.content;
                    // console.log(response); axios 拦截器已使用
                }
            );
        });

        //返回数据
        return {
            ebooks,
            // ebooks2: toRef(ebooks1, "books"), //toRef把books变成响应式变量,定义变量名ebooks2
            // listData,
            pagination: {
                onChange: (page: any) => {
                    console.log(page);
                },
                pageSize: 3,
            },
            actions: [
                {type: 'StarOutlined', text: '156'},
                {type: 'LikeOutlined', text: '156'},
                {type: 'MessageOutlined', text: '2'},
            ],
            handleClick,
            level1,
        }
    }
});
</script>
<!--scoped表示样式只在当前组件起作用-->
<style scoped>
.ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
}
</style>