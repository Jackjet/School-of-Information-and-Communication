// const PortUrl = 'http://www.liangxin.net.cn/liangxin_new/' // 生产地址
const PortUrl = 'http://localhost:8016/'
// const PortUrl = 'http://39.102.73.127/liangxin_new/' // 测试地址

const constApi = {
  /**
   * @login 用户登录
   * @PutPassWord  修改用户密码
   */
  UserLogin: {
    login: PortUrl + 'webadmin/webAdminUser/signIn',
    PutPassWord: PortUrl + 'webadmin/webAdminUser/updatePwd'
  },

  courseManage: {
    questiongroup: {
      find: PortUrl + 'webadmin/examinationGroup/findAll',
      insert: PortUrl + 'webadmin/examinationGroup/insert',
      update: PortUrl + 'webadmin/examinationGroup/update',
      delete: PortUrl + '/webadmin/examinationGroup/deleteById'
    },
    question: {
      find: PortUrl + 'webadmin/question/findAll',
      findById: PortUrl + 'webadmin/question/findById',
      insert: PortUrl + 'webadmin/question/insert',
      update: PortUrl + 'webadmin/question/update',
      delete: PortUrl + 'webadmin/question/deleteByIds',
      move: PortUrl + 'webadmin/question/moveByIds'
    },
    examination: {
      findGroup: PortUrl + 'webadmin/examination/findGroup',
      find: PortUrl + 'webadmin/examination/findAll',
      findById: PortUrl + 'webadmin/examination/findById',
      insert: PortUrl + 'webadmin/examination/insert',
      update: PortUrl + 'webadmin/examination/update',
      delete: PortUrl + 'webadmin/examination/deleteById',
      move: PortUrl + 'webadmin/examination/moveByIds'
    }
  }
}

export default {
  constApi: constApi
}
