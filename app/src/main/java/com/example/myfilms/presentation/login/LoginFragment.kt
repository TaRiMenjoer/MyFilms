package com.example.myfilms.presentation.login

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myfilms.R
import com.example.myfilms.data.model.LoginApprove
import com.example.myfilms.databinding.FragmentLoginBinding
import com.example.myfilms.presentation.common.Utils.LoadingState
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding
            ?: throw RuntimeException(getString(R.string.fragment_login_binding_is_null))

    private val viewModel by viewModel<ViewModelLogin>()
    private lateinit var prefSettings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        prefSettings = context?.getSharedPreferences(
            APP_SETTINGS,
            Context.MODE_PRIVATE
        ) as SharedPreferences

        if (prefSettings.getString(SESSION_ID_KEY, null) != null) {
            findNavController().navigate(R.id.action_login_fragment_to_movies_nav)
        }
        editor = prefSettings.edit()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        addTextChangeListeners()
        onLoginClick()
    }


    private fun onLoginClick() {
        binding.btnLogin.setOnClickListener {
            hideKeyboard(requireActivity())


            viewModel.resetErrorInputPassword()
            viewModel.resetErrorInputUserName()

            val userName = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val data = LoginApprove(
                username = userName,
                password = password,
                request_token = ""

            )
            viewModel.login(data, userName, password)
            observeLoadingState()

        }

    }

    private fun observeViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                "Неверные данные"
            } else {
                null
            }
            binding.tilPassword.error = message
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                "Неверные данные"
            } else {
                null
            }
            binding.tilName.error = message
        }

    }

    private fun addTextChangeListeners() {
        binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputUserName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputPassword()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }


    private fun observeLoadingState() {
        viewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                LoadingState.IS_LOADING -> binding.pbLoading.visibility = View.VISIBLE
                LoadingState.FINISHED -> binding.pbLoading.visibility = View.GONE
                LoadingState.SUCCESS -> {
                    viewModel.sessionId.observe(viewLifecycleOwner) {
                        sessionId = it
                        putDataIntoPref(sessionId)
                        try {
                            findNavController().navigate(R.id.action_login_fragment_to_movies_nav)
                        } catch (e: Exception) {
                        }
                    }
                }
                else -> Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun putDataIntoPref(string: String) {
        editor.putString(SESSION_ID_KEY, string)
        editor.commit()
        binding.etUsername.text = null
        binding.etPassword.text = null
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    companion object {
        private var sessionId: String = ""
        const val APP_SETTINGS = "Settings"
        const val SESSION_ID_KEY = "SESSION_ID"
    }
}

