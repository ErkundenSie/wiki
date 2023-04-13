<template>
    <a-layout>
        <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <p><!--添加查询框-->
                <a-form layout="inline" :model="param"><!--调用param-->
                    <a-form-item>
                        <a-button type="primary" @click="handleQuery()">
                            查询
                        </a-button>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="add()">
                            新增
                        </a-button>
                    </a-form-item>
                </a-form>
            </p>

            <a-table
                :columns="columns"
                :row-key="record => record.id"
                :data-source="level1"
                :loading="loading"
                :pagination="false"
            ><!--除了后端删除分页数据前端:pagination="false"关闭分页，数据来源文档后的level1-->
                <template #cover="{ text: cover }">
                    <img v-if="cover" :src="cover" alt="avatar"/>
                </template>
                <template v-slot:action="{ text, record }">
                    <!--a-space空格组件-->
                    <a-space size="small">
                        <a-button type="primary" @click="edit(record)">
                            编辑
                        </a-button><!--handleDelete不能起名关键字-->
                        <a-popconfirm
                            title="删除后不可恢复，确认删除"
                            ok-text="是"
                            cancel-text="否"
                            @confirm="handleDelete(record.id)"
                        >
                            <a-button type="danger">
                                删除
                            </a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>
    <a-modal
        title="文档表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
        <!--model绑定doc在下面定义-->
        <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <a-form-item label="名称">
                <a-input v-model:value="doc.name"/>
            </a-form-item>
            <a-form-item label="父文档">
                <!--treeSelectData是level1的复制修改版防止修改表单未保存时，表数据改变-->
                <a-tree-select
                    v-model:value="doc.parent"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    :tree-data="treeSelectData"
                    placeholder="请选择父文档"
                    tree-default-expand-all
                    :replaceFields="{title:'name',key:'id',value:'id'}"
                ><!--属性转换-->
                </a-tree-select>
            </a-form-item>
            <a-form-item label="顺序">
                <a-input v-model:value="doc.sort"/>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";


export default defineComponent({
    name: 'AdminDoc',
    setup() {
        const route = useRoute();//获取路由信息
        const param = ref();
        param.value = {}; //定义并初始化param
        const docs = ref();

        const loading = ref(false);

        const columns = [
            {
                title: '名称',
                dataIndex: 'name'
            },
            {
                title: '父文档',
                key: 'parent',
                dataIndex: 'parent',
            },
            {
                title: '顺序',
                dataIndex: 'sort'
            },
            {
                title: 'Action',
                key: 'action',
                slots: {customRender: 'action'}
            }
        ];


        /**
         * 数据查询所有
         * 并转成树形结构
         **/
        const level1 = ref();//一级文档数，children属性是二级
        const handleQuery = () => {
            loading.value = true;
            level1.value = [];//数据清空
            //把params中的page、size传到后端，只写params:params传全部
            axios.get("/doc/all").then((response) => {
                loading.value = false;
                const data = response.data;
                if(data.success) {
                    docs.value = data.content;
                    console.log("原始数组：",docs.value);
                    level1.value = [];
                    level1.value = Tool.array2Tree(docs.value,0); //数据库一级文档对应的父id是000即0
                    console.log("树形结构：",level1);
                }else {
                    message.error(data.message) //data.message 返回后端的自定义的异常处理
                }

            });
        };

        /**
         * 表单
         * 定义变量及方法
         * 保存
         */
        const treeSelectData = ref();
        treeSelectData.value = [];
        const doc = ref({});
        const modalVisible = ref(false);
        const modalLoading = ref(false);
        const handleModalOk = () => {
            modalLoading.value = true; //先弹出窗口
            axios.post("/doc/save",doc.value).then((response) => {
                modalLoading.value = false;//只要后端有返回就去掉
                const data = response.data; // data = commonResp
                if(data.success) {
                    //保存成功拿到结果后关闭窗口重新加载列表
                    modalVisible.value = false;
                    handleQuery();
                }else {
                    message.error(data.message);
                }
            });
        };
        /**
         * 新增
         */
        const add = () => {
            modalVisible.value = true;
            // 通过route获取新增文档时电子书id参数ebookId
            doc.value = {
                ebookId: route.query.ebookId
            };
            treeSelectData.value = Tool.copy(level1.value);
            treeSelectData.value.unshift({id: 0, name: '无'})
        };
        /**
         * 编辑
         */
        const edit = (record: any) => {
            modalVisible.value = true;
            doc.value = Tool.copy(record); //修改表单时是对复制出的新对象进行修改=》不保存会变修改的问题
            treeSelectData.value = Tool.copy(level1.value);
            setDisable(treeSelectData.value, record.id);//不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
            treeSelectData.value.unshift({id: 0, name: '无'});//为选择树添加一个“无”,unshift在数组前面添加元素
        };
        /**
         * 删除
         */
        const handleDelete = (id: number) => {
            // console.log(level1.value,id);//level1响应式变量加value
            getDeleteIds(level1.value,id);//先查询查找整根树枝
            axios.delete("/doc/delete/" + ids.join(",")).then((response) => {//把ids变成以,分隔的字符串
                const data = response.data; // data = commonResp
                if(data.success) {
                    //重新加载列表
                    handleQuery();
                }
            });
        };
        /**
         * 将某一个节点和他的子孙节点都设置为disabled
         */
        const setDisable = (treeSelectData: any, id: any) => {
            //遍历数组，即遍历某一层节点
            for (let i = 0; i < treeSelectData.length; i++) {
                const node = treeSelectData[i];
                if (node.id === id) {//找到id将目标节点设置为disable就不可编辑了
                    node.disabled = true;
                    const children = node.children;//拿出children
                    if (Tool.isNotEmpty(children)) {//遍历子节点，设置不可编辑
                        for (let j = 0; j < children.length; j++) {
                            setDisable(children, children[j].id)
                        }
                    }
                } else {//如果当前节点不是目标节点，则到其子节点再找找看
                    const children = node.children;
                    if (Tool.isNotEmpty(children)) {
                        setDisable(children, id);
                    }
                }
            }
        };
        /**
         * 模仿上面循环获得子孙节点,查找整根树枝
         */
        const ids: Array<string> = [];//放到该数组
        const getDeleteIds = (treeSelectData: any, id: any) => {
            for (let i = 0; i < treeSelectData.length; i++) {
                const node = treeSelectData[i];
                if (node.id === id) {//当前节点=目标节点
                    console.log("disabled", node);
                    ids.push(id);
                    const children = node.children;
                    if (Tool.isNotEmpty(children)) {
                        for (let j = 0; j < children.length; j++) {
                            getDeleteIds(children, children[j].id)
                        }
                    }
                } else {
                    const children = node.children;
                    if (Tool.isNotEmpty(children)) {
                        getDeleteIds(children, id);
                    }
                }
            }
        };

        /**
         * 周期函数
         */
        //初始查一次，返回到上面handleQuery方法，page,size需要和后端对应
        onMounted(() => {
            handleQuery();
        });

        return {
            //表格的
            param,
            //docs,
            level1,
            columns,
            loading,
            handleQuery,

            edit,
            add,
            handleDelete,

            //表单类
            doc,
            modalVisible,
            modalLoading,
            handleModalOk,
            treeSelectData,

        }
    }
});
</script>

<style scoped>
img {
    width: 50px;
    height: 50px;
}
</style>
