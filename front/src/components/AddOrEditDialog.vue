<template>
  <div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <el-form label-position="right" label-width="80px" :model="form" v-else>
      <el-form-item label="姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="培训编号">
        <el-input v-model="form.memberId" />
      </el-form-item>
      <el-form-item label="竞赛编号">
        <el-input v-model="form.teamId" />
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button type="primary" v-if="type === 'add'">添加</el-button>
      <el-button type="success" v-else>修改</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, reactive, ref } from "vue";
export default defineComponent({
  props: {
    type: {
      type: String,
    },
    userInfo: {
      type: Object,
    },
  },
  setup(props) {
    const skeletonFlag = ref(true);
    const form = reactive({
      name: "",
      email: "",
      memberId: "",
      teamId: "",
    });

    const type = computed(() => {
      return props.type;
    });

    onMounted(() => {
      if (props.type === "edit") {
        form.name = (props.userInfo as any).name;
        form.email = (props.userInfo as any).email;
        form.memberId = (props.userInfo as any).memberId;
        form.teamId = (props.userInfo as any).teamId;
      }
      skeletonFlag.value = false;
    });

    return {
      form,
      type,
      skeletonFlag,
    };
  },
});
</script>

<style lang="scss" scoped>
.btn {
  text-align: center;
}
</style>