package com.example.myapplication.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.MyApplication
import com.example.myapplication.entity.RegionBean
import com.example.myapplication.entity.ResponseBase
import com.example.myapplication.entity.UserBean
import com.example.myapplication.entity.UserRewardBean
import com.example.myapplication.http.RegionUtils
import com.example.myapplication.http.UserRewardUtils
import com.example.myapplication.http.UserUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.livebus.LiveDataBus
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object UserViewModel {

    var user: UserBean? = null
        set(value) { // setter
            field = value
            LiveDataBus.send("livebus_user_change", field)
        }

    var region: RegionBean? = null
        set(value) { // setter
            field = value
            LiveDataBus.send("livebus_region_change", field)
        }

    init {
        requestUserInfo()
        requestRegionInfo()
    }

    private fun requestUserInfo() {
        user = try {
            Gson().fromJson(Prefs.userInfo, UserBean::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun requestRegionInfo() {
        val json = "{\"id\":1,\"name\":\"梦暴科技馆\",\"account\":\"999\",\"password\":\"mb886699\",\"contactName\":\"唐辉\",\"contactPhone\":\"111\",\"address\":\"@@@\",\"enable\":1,\"pin\":\"AAA\"}"

        region = try {
            Gson().fromJson(json, RegionBean::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun getAllUsers(viewModelScope: LifecycleCoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            val subList = try {
                UserUtils.getAllUsers()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            LiveDataBus.send("getAllUsers", subList)
        }
    }

    fun getAllRegion(viewModelScope: LifecycleCoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            val subList = try {
                RegionUtils.getAll()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            LiveDataBus.send("getAllRegion", subList)
        }
    }

    fun login(viewModelScope: LifecycleCoroutineScope, username: String, password: String, saveState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                UserUtils.login(username, password)
            } catch (e: Exception) {
                null
            }

            if (responseBase != null) {
                if (responseBase.code == 200) {
                    Prefs.isSaveStatus = saveState
                    Prefs.userInfo = Gson().toJson(responseBase.data)
                    Prefs.isLoginFromPhone = false

                    LiveDataBus.send("livebus_login", Pair(true, "登录成功！"))
                } else {
                    Prefs.isSaveStatus = false
                    Prefs.userInfo = ""

                    LiveDataBus.send("livebus_login", Pair(false, responseBase.message))
                }

                requestUserInfo()
            } else {
                LiveDataBus.send("livebus_login", Pair(false, "login failed: response body is null"))
            }
        }
    }

    fun loginWithPhone(viewModelScope: LifecycleCoroutineScope, phone: String, saveState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                UserUtils.loginWithPhone(phone, region?.pin!!)
            } catch (e: Exception) {
                null
            }

            if (responseBase != null) {
                if (responseBase.code == 200) {
                    Prefs.isSaveStatus = saveState
                    Prefs.userInfo = Gson().toJson(responseBase.data)
                    Prefs.isLoginFromPhone = true

                    LiveDataBus.send("livebus_login", Pair(true, "登录成功！"))
                } else {
                    Prefs.isSaveStatus = false
                    Prefs.userInfo = ""

                    LiveDataBus.send("livebus_login", Pair(false, "自动登录失败！${responseBase.message}"))
                }

                requestUserInfo()
            } else {
                LiveDataBus.send("livebus_login", Pair(false, "auto login failed: response body is null"))
            }
        }
    }

    fun loginWithAdmin(viewModelScope: LifecycleCoroutineScope, phone: String, saveState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                UserUtils.loginWithPhone(phone, region?.pin!!)
            } catch (e: Exception) {
                null
            }

            if (responseBase != null && responseBase.code == 200) {
                val newUser = Gson().fromJson(Gson().toJson(responseBase.data), UserBean::class.java)
                if (newUser.role == 2) {  //  是管理员
                    Prefs.isSaveStatus = saveState
                    Prefs.userInfo = Gson().toJson(responseBase.data)
                    Prefs.isLoginFromPhone = true
                    requestUserInfo()

                    LiveDataBus.send("livebus_login", Pair(true, "管理员登录成功！"))
                } else {
                    LiveDataBus.send("livebus_login", Pair(false, "您不是管理员，请重新申请或等待系统确认！"))
                }
            } else {
                LiveDataBus.send("livebus_login", Pair(false, "登录失败，${responseBase?.message}"))
            }
        }
    }

    fun verify(viewModelScope: LifecycleCoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                if (Prefs.isLoginFromPhone) {
                    UserUtils.loginWithPhone(user?.phone.toString(), region?.pin!!)
                } else {
                    UserUtils.login(user?.account.toString(), user?.password.toString())
                }
            } catch (e: Exception) {
                null
            }

            if (responseBase != null && responseBase.code == 200) {
                Prefs.userInfo = Gson().toJson(responseBase.data)
            } else {
                Prefs.userInfo = ""
            }
            requestUserInfo()
        }
    }
    fun register(viewModelScope: LifecycleCoroutineScope, username: String, password: String, phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                UserUtils.register(username, password, phone, region?.pin!!)
            } catch (e: Exception) {
                null
            }

            if (responseBase != null) {
                when (responseBase.code) {
                    200 -> {
                        Prefs.isSaveStatus = true
                        Prefs.userInfo = Gson().toJson(responseBase.data)
                        Prefs.isLoginFromPhone = false
                        requestUserInfo()

                        LiveDataBus.send("livebus_login", Pair(true, "注册成功，已登录！"))
                    }
                    else -> {
                        Prefs.isSaveStatus = false
                        Prefs.userInfo = ""
                        requestUserInfo()

                        LiveDataBus.send("livebus_login", Pair(false, responseBase.message))
                    }
                }
            } else {
                LiveDataBus.send("livebus_login", Pair(false, "register failed: response body is null"))
            }
        }
    }

    fun logout(viewModelScope: LifecycleCoroutineScope, account: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val b: Boolean = try {
                UserUtils.logout(account, password)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }

            if (b) {
                Prefs.userInfo = ""
                Prefs.isSaveStatus = false
                requestUserInfo()

                LiveDataBus.send("livebus_login", Pair(true, "注销成功"))
            } else {
                LiveDataBus.send("livebus_login", Pair(false, "注销失败"))
            }
        }
    }

    fun getReward(viewModelScope: LifecycleCoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            val reward: Int = try {
                UserRewardUtils.getReward(
                    Gson().toJson(
                        UserRewardBean().apply {
                            this.uid = user?.id
                            this.pin = region?.pin
                        }
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }

            LiveDataBus.send("livebus_get_reward", reward)
        }
    }

    fun modifyRole(viewModelScope: LifecycleCoroutineScope, account: String, role: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val state = UserUtils.modifyRole(account, role)

            LiveDataBus.send("livebus_modify_role", state)
        }
    }

    fun modifyPin(viewModelScope: LifecycleCoroutineScope, adminBean: RegionBean, account: String, pin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newUser: ResponseBase? = try {
                UserUtils.modifyPin(account, pin)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            if (newUser != null && newUser.code == 200) {
                /*Prefs.adminInfo = Gson().toJson(adminBean)*/

                Prefs.userInfo = Gson().toJson(newUser.data)
                requestUserInfo()
               /*
                    requestRegionInfo()  // 切换场所，暂时不需要
                */
                LiveDataBus.send("livebus_modify_pin", Pair(true, "切换成功"))
            } else {
                LiveDataBus.send("livebus_modify_pin", Pair(false, "切换失败，${newUser?.message}"))
            }
        }
    }

    fun applyAdmin(viewModelScope: LifecycleCoroutineScope, account: String, password: String, admin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val flag = try {
                UserUtils.applyAdmin(account, password, admin)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }

            LiveDataBus.send("livebus_apply_admin", flag)
        }
    }
}