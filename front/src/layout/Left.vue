<template>
  <div class="left-main">
    <el-menu
      active-text-color="#ffd04b"
      background-color="#545c64"
      class="el-menu-vertical-demo"
      default-active="1"
      text-color="#fff"
    >
      <el-menu-item index="1" @click="menuClick">
        <el-icon><User /></el-icon>
        <span>用户管理</span>
      </el-menu-item>
      <el-menu-item index="2" @click="menuClick">
        <el-icon><Document /></el-icon>
        <span>作业管理</span>
      </el-menu-item>
      <el-menu-item index="3" @click="menuClick">
        <el-icon><Menu /></el-icon>
        <span>应用竞赛</span>
      </el-menu-item>
      <el-menu-item index="4" @click="menuClick">
        <el-icon><setting /></el-icon>
        <span>开发竞赛</span>
      </el-menu-item>
      <el-menu-item index="5" @click="menuClick">
        <el-icon><CreditCard /></el-icon>
        <span>证书管理</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import router from "@/router";
export default defineComponent({
  setup() {
    const menuClick = (val: any) => {
      if (val.index === "1") {
        router.push({ path: "/user" });
      } else if (val.index === "2") {
        router.push({ path: "/homework" });
      } else if (val.index === "3") {
        router.push({ path: "/apply" });
      } else if (val.index === "4") {
        router.push({ path: "/develop" });
      } else if (val.index === "5") {
        router.push({ path: "/cert" });
      }
    };

    return {
      menuClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.left-main {
  height: 100%;
  .el-menu {
    height: 100%;
  }
}
</style>