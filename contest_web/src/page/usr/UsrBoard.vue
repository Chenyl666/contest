<template>
  <div class="board">
    <router-view @to-login="$emit('to-login')" @to-success="toSuccess"/>
<!--    <OperationSuccess @to-login="$emit('to-login')"/>-->
  </div>
</template>

<script setup>
import router from "@/router/router";

const toSuccess = (arg) => {
  router.push({path:'/usr/success',query:{operation: arg}})
}


</script>

<style scoped>
  .board{
    /*border: solid black;*/
    width: 24em;
    height: 27em;
    margin-left: 35em;
    margin-top: 4.5em;
    box-shadow: 0 5px 10px 0 rgb(0 0 0 / 10%);
    border-radius: 6px
  }
</style>